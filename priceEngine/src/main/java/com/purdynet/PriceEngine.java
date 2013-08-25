package com.purdynet;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.conditions.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.scaling.Scaling;
import com.purdynet.scaling.impl.LogScaling;
import com.purdynet.scaling.impl.PercentScaling;
import com.purdynet.scaling.impl.TraditionalScaling;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/16/13
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceEngine
{
    private static final TraditionalScaling ts = new TraditionalScaling();
    private static final LogScaling ls = new LogScaling(.005);
    private static final PercentScaling ps = new PercentScaling(0.01);

    public static void main(String[] args) throws Exception
    {
        PriceEngine pe = new PriceEngine();
        pe.run(args);
    }

    public void run(String[] args) throws Exception
    {
        Downloader d = new YahooDownloader();
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1);
        Date startDate = c.getTime();
        //File test = d.download("V", startDate, new Date());

        verifyArgs(args);
        File file = getFile(args);

        final Condition tCond = new Condition(ps, 25, 180, 2, 8);

        List<PriceRecord> prices = null;

        //prices = parseFile(new File("/home/dnpurdy/pf/GOOG.csv"));
        //whenPatternFound(ls, prices, "5X-40-15X-");

        final Map<String, OccuranceFreq> occuranceFreqMap = new ConcurrentHashMap<String, OccuranceFreq>();

        List<String> testSymbols = Arrays.asList("BAC",
                "GE",
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
                "HMA"
                );

        /*for(final String securitySym : testSymbols)
        {
            File dl = d.download(securitySym, startDate, new Date());
            File dest = new File("/home/dnpurdy/pf/"+securitySym+".csv");
            FileUtils.copyFile(dl,dest);
        }*/

        ExecutorService es = Executors.newFixedThreadPool(4);
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
        for(final String securitySym : testSymbols)
        {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    try {
                        List<PriceRecord> prices = parseFile(new File("/home/dnpurdy/pf/" + securitySym + ".csv"));
                        computeFreq(tCond.getTestScaling(), securitySym, prices, tCond, occuranceFreqMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        }
        es.invokeAll(tasks);
        es.shutdown();

        List<OccuranceFreq> occurs = new ArrayList<OccuranceFreq>(occuranceFreqMap.values());
        Collections.sort(occurs);



        BufferedWriter out = new BufferedWriter(new FileWriter(new File("/tmp/download.csv")));
        for(OccuranceFreq f : occurs)
        {
            if(f.score()>0)
            {
                //System.out.println(f.getPattern()+": "+f.happened()+" "+f.percentGood().toString()+" --> "+f.score()+
                //"  --"+f.getSymbolsHappened()+"-"+f.getSymbolsGood());
                out.write(f.getPattern()+": "+f.happened()+" "+f.percentGood().toString()+" --> "+f.score()+
                          "  --"+f.getSymbolsHappened()+"-"+f.getSymbolsGood());
                out.write("\n");
            }

        }
        out.close();
        /*for(Map.Entry<String, OccuranceFreq> entry : occuranceFreqMap.entrySet())
        {
            if(entry.getValue().percentGood()>0)
                System.out.println(entry.getKey()+": "+entry.getValue().happened()+" "+entry.getValue().percentGood().toString()+" --> "+entry.getValue().score());
        }
*/
        //System.out.println(t.percentGood());

        //pf.render();
    }

    private void whenPatternFound(Scaling s, List<PriceRecord> prices, String pattern)
    {
        Collections.sort(prices);
        List<PriceRecord> pricesSoFar = new ArrayList<PriceRecord>();

        for(int prIdx = 0; prIdx<prices.size(); prIdx++)
        {
            pricesSoFar.add(prices.get(prIdx));
            PointFigure pf = new PointFigure(pricesSoFar, s);

            int patternLen = 0;
            String workingPattern = pattern;
            while(workingPattern.contains("-"))
            {
                patternLen++;
                workingPattern = workingPattern.replaceFirst("-","");
            }

            if(pf.getNumberOfColumns()<patternLen) continue;
            String currentPatternCode = pf.getPatternCode(patternLen);
            System.out.println(prIdx+","+currentPatternCode);
            if(currentPatternCode.equals(pattern)) System.out.println(prIdx);
        }
    }

    private void computeFreq(Scaling s, String symbol, List<PriceRecord> prices, Condition testCondition, Map<String, OccuranceFreq> occuranceFreqMap)
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
                    OccuranceFreq of = new OccuranceFreq(patternCode);
                    if(occuranceFreqMap.containsKey(patternCode)) of = occuranceFreqMap.get(patternCode);
                    if(achievedReturn) of.happenedGood(symbol);
                    else of.happenedNoGood(symbol);
                    occuranceFreqMap.put(patternCode, of);
                    patternLocations.get(patternCode).add(pf.getNumberOfColumns());
                }
            }
        }


    }

    public void verifyArgs(String[] args) throws Exception
    {
        if(args==null || args.length<1 || args.length>2)
        {
            throw new Exception("Bad args!");
        }
    }

    public File getFile(String[] args)
    {
        return new File(args[0]);
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


}
