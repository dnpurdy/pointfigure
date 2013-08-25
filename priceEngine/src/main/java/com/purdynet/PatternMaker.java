package com.purdynet;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.conditions.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.scaling.Scaling;
import com.purdynet.scaling.impl.LogScaling;
import com.purdynet.scaling.impl.PercentScaling;
import com.purdynet.scaling.impl.TraditionalScaling;
import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

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

        results.setCondition(tCond);
        results.setSymbols(Arrays.asList("GE",
                "F",
                "AA",
                "PFE",
                "COLE",
                "T",
                "C",
                "PHM",
                "ABX",
                "ELN",
                "VALE",
                "KO",
                "SNV",
                "ORCL",
                "PBR",
                "P",
                "EMC",
                "JPM",
                "KGC",
                "RAD",
                "JCP",
                "WFC",
                "BSX",
                "S",
                "VZ",
                "ITUB",
                "FCX",
                "HPQ",
                "CHK",
                "ALU",
                "IAG",
                "RF",
                "JNJ",
                "MRK",
                "DHI",
                "SID",
                "NEM",
                "ANR",
                "GM",
                "POT",
                "XOM",
                "DAL",
                "GLW",
                "AMD",
                "NLY",
                "LPR",
                "ACI",
                "TSM",
                "AUY",
                "NOK",
                "SLW",
                "MS",
                "AIG",
                "GG",
                "ABT",
                "X",
                "XRX",
                "HST",
                "ECA",
                "LEN",
                "SAN",
                "CLF",
                "PG",
                "ARR",
                "HD",
                "EGO",
                "MT",
                "HK",
                "BMY",
                "GGB",
                "LOW",
                "MO",
                "HL",
                "DIS",
                "LCC",
                "WLT",
                "HAL",
                "KEY",
                "SWY",
                "MOS",
                "M",
                "FMD",
                "SU",
                "WMT",
                "TWO",
                "ODP",
                "GFI",
                "EXC",
                "MTG",
                "KBH",
                "SCHW",
                "COP",
                "AU",
                "CX",
                "TLM",
                "USB",
                "MGM",
                "HMA" ));

        final Map<String, Pattern> occuranceFreqMap = new ConcurrentHashMap<String, Pattern>();

        ExecutorService es = Executors.newFixedThreadPool(4);
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
        for(final String securitySym : results.getSymbols())
        {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    try {
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

        PatternContext.writeToFile(results, "quarks.ser");
    }

    private void writeToFile() throws FileNotFoundException, IOException
    {

    }

    public List<PriceRecord> parseFile(File input) throws IOException
    {
        List<PriceRecord> ret = new ArrayList<PriceRecord>();
        CSVReader reader = new CSVReader(new FileReader(input));
        String[] line;
        try {
            //Burn header
            line = reader.readNext();
            while((line = reader.readNext()) != null)
            {
                PriceRecord pr = new PriceRecord(line[0], new BigDecimal(line[6]));
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
        List<PriceRecord> pricesSoFar = new ArrayList<PriceRecord>();
        Map<String,Set<Integer>> patternLocations = new HashMap<String,Set<Integer>>();

        for(int prIdx = 0; prIdx<prices.size(); prIdx++)
        {
            pricesSoFar.add(prices.get(prIdx));
            PointFigure pf = new PointFigure(pricesSoFar, s);

            if(pf.getNumberOfColumns()<testCondition.getMinPatternSize()+2) continue;

            int maxPatternSize = Math.min(testCondition.getMaxPatternSize(), pf.getNumberOfColumns() - testCondition.getMinPatternSize());
            boolean achievedReturn = false;
            BigDecimal runningPrice = prices.get(prIdx).getPrice();
            for(int futureIdx = prIdx; futureIdx<=Math.min(prIdx+testCondition.getDaysHorizon(), prices.size()-1); futureIdx++)
            {
                BigDecimal runningReturn = prices.get(futureIdx).getPrice().subtract(runningPrice).divide(runningPrice, 3, BigDecimal.ROUND_HALF_EVEN);
                int intReturn = runningReturn.multiply(new BigDecimal(100)).intValue();
                if(intReturn>testCondition.getPercentReturn())
                {
                    achievedReturn = true;
                    break;
                }
            }

            for(int patternSize = testCondition.getMinPatternSize(); patternSize <= maxPatternSize; patternSize++)
            {
                String patternCode = pf.getPatternCode(patternSize);

                //Add pattern location if we've never seen it before
                if(patternLocations.get(patternCode)==null)
                {
                    HashSet<Integer> seenLocations = new HashSet<Integer>();
                    patternLocations.put(patternCode,seenLocations);
                }

                if(!patternLocations.get(patternCode).contains(pf.getNumberOfColumns()))
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
                    patternLocations.get(patternCode).add(pf.getNumberOfColumns());
                }
            }
        }


    }
}
