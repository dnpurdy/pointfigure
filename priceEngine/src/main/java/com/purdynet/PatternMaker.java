package com.purdynet;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.conditions.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.graph.PFColumn;
import com.purdynet.graph.PointFigureGraph;
import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.Scaling;
import com.purdynet.scaling.impl.PercentScaling;
import com.purdynet.util.PFColumnUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/23/13
 * Time: 12:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatternMaker
{
    private static final PercentScaling ps = new PercentScaling(0.01);

    private static final PatternContext results = new PatternContext();

    public static void main(String[] args) throws Exception
    {
        PatternMaker pe = new PatternMaker();
        pe.run(args);
    }

    public void run(String[] args)
    {
        final Condition tCond = new Condition(ps, 25, 180, 2, 8);

        //List<String> symbols = readSymbolFile("/home/dnpurdy/Desktop/spsym.csv");
        List<String> symbols = Arrays.asList("WAG");

        results.setCondition(tCond);
        results.setSymbols(symbols);

        final Map<String, Pattern> occuranceFreqMap = new ConcurrentHashMap<String, Pattern>();

        ExecutorService es = Executors.newFixedThreadPool(8);
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
        for(final String securitySym : results.getSymbols())
        {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    try {
                        System.out.println(securitySym);
                        Downloader d = new YahooDownloader();
                        Calendar c = Calendar.getInstance();
                        c.set(2000, 0, 1);
                        Date startDate = c.getTime();
                        File dl = d.download(securitySym, startDate, new Date());
                        File dest = new File("/home/dnpurdy/pf/"+securitySym+".csv");
                        FileUtils.copyFile(dl, dest);
                        List<PriceRecord> prices = parseFile(new File("/home/dnpurdy/pf/" + securitySym + ".csv"));
                        computeFreq(results.getCondition().getTestScaling(), securitySym, prices, results.getCondition(), occuranceFreqMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        }

        try {
           es.invokeAll(tasks);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally {
            es.shutdown();
        }

        results.setPatterns(occuranceFreqMap);
        for(Map.Entry<String,Double> entry : results.getScores().entrySet())
        {
            if(entry.getValue()>0) System.out.println(entry.getKey()+": "+entry.getValue());
        }
        //PatternContext.writeToFile(results, "test.ser");
    }

    private void writeToFile() throws FileNotFoundException, IOException
    {

    }

    public List<PriceRecord> parseFile(File input) throws IOException
    {
        List<PriceRecord> ret = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(input));
        String[] line;
        try {
            //Burn header
            line = reader.readNext();
            while((line = reader.readNext()) != null)
            {
                PriceRecord pr = new PriceRecord(line[0], new scala.math.BigDecimal(new BigDecimal(line[6])));
                ret.add(pr);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            reader.close();
        }
        return ret;
    }

    private void computeFreq(Scaling s, String symbol, List<PriceRecord> prices, Condition testCondition, Map<String, Pattern> occuranceFreqMap)
    {
        Collections.sort(prices);
        Map<String,Set<Integer>> patternLocations = new HashMap<String,Set<Integer>>();

        PointFigureGraph pfg = new PointFigureGraph(prices, s);

        for(int prIdx = 0; prIdx<prices.size(); prIdx++)
        {
            List<PFColumn> curCols = pfg.getCurCols(prices.get(prIdx).getDateStr());
            if(curCols.size()<testCondition.getMinPatternSize()+2) continue;

            int maxPatternSize = Math.min(testCondition.getMaxPatternSize(), curCols.size() - testCondition.getMinPatternSize());
            boolean achievedReturn = false;
            BigDecimal runningPrice = prices.get(prIdx).getJavaPrice();
            for(int futureIdx = prIdx; futureIdx<=Math.min(prIdx+testCondition.getDaysHorizon(), prices.size()-1); futureIdx++)
            {
                BigDecimal runningReturn = prices.get(futureIdx).getJavaPrice().subtract(runningPrice).divide(runningPrice, 3, BigDecimal.ROUND_HALF_EVEN);
                int intReturn = runningReturn.multiply(new BigDecimal(100)).intValue();
                if(intReturn>testCondition.getPercentReturn())
                {
                    achievedReturn = true;
                    break;
                }
            }

            for(int patternSize = testCondition.getMinPatternSize(); patternSize <= maxPatternSize; patternSize++)
            {
                String patternCode = PFColumnUtil.getPattern(curCols, patternSize);

                //Add pattern location if we've never seen it before
                if(patternLocations.get(patternCode)==null)
                {
                    HashSet<Integer> seenLocations = new HashSet<Integer>();
                    patternLocations.put(patternCode,seenLocations);
                }

                if(!patternLocations.get(patternCode).contains(curCols.size()))
                {
                    Pattern of = new Pattern();
                    of.setPattern(patternCode);
                    if(occuranceFreqMap.containsKey(patternCode)) of = occuranceFreqMap.get(patternCode);
                    if(achievedReturn)
                    {
                        of.setTimesSuccessful(of.getTimesSuccessful()+1);
                        of.setTimesSeen(of.getTimesSeen()+1);
                        of.addSymbolSeen(symbol);
                        of.addSymbolSuccessful(symbol);
                    }
                    else
                    {
                        of.setTimesSeen(of.getTimesSeen()+1);
                        of.addSymbolSeen(symbol);
                    }
                    occuranceFreqMap.put(patternCode, of);
                    patternLocations.get(patternCode).add(curCols.size());
                }
            }
        }


    }

    public List<String> readSymbolFile(String filename)
    {
        List<String> ret = new ArrayList<String>();

        FileReader fis;
        BufferedReader reader = null;
        String line;
        try {
            //Burn header
            fis = new FileReader(filename);
            reader = new BufferedReader(fis);
            while((line = reader.readLine()) != null)
            {
                ret.add(line);
            }
            reader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}
