package com.purdynet.app;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.purdynet.condition.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.graph.PFColumn;
import com.purdynet.graph.PointFigureGraph;
import com.purdynet.pattern.PatternContext;
import com.purdynet.pattern.PatternInstance;
import com.purdynet.persistence.MongoUtils;
import com.purdynet.persistence.PatternDAO;
import com.purdynet.persistence.SymbolDAO;
import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.Scaling;
import com.purdynet.scaling.impl.PercentScaling;
import com.purdynet.util.DateUtil;
import com.purdynet.util.FileUtil;
import com.purdynet.util.PFColumnUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/31/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextCreator
{
    private static final PatternContext results = new PatternContext();
    private String filename;

    public static void main(String[] args) throws Exception
    {
        ContextCreator cc = new ContextCreator();
        cc.parseArgs(args);
        cc.run();
    }

    public void parseArgs(String[] args)
    {
        if(args.length==0 || args.length>1) System.exit(1);
        this.filename = args[0];
    }

    public void run()
    {
        List<String> symbols = FileUtil.getSymbolsFromFile(filename);
        final Condition tCond = new Condition(new PercentScaling(0.01), 25, 120, 2, 15);
        final Map<String, PatternInstance> occuranceFreqMap = new ConcurrentHashMap<String, PatternInstance>();

        MongoClient mongoClient = MongoUtils.getClient();
        String runId = String.valueOf(System.currentTimeMillis());
        DB db = mongoClient.getDB(runId);

        symbols = FileUtil.getSymbolsFromFile(filename);
        //symbols = Arrays.asList("WAG","EBAY","ECL","EIX","EW","EA","EMC","EMR","ESV","ETR","EOG","EQT","EFX","EQR");

        for(String symbol : symbols)
        {
            SymbolDAO.create(db, "symbols", symbol);
        }

        results.setCondition(tCond);
        results.setSymbols(symbols);

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
                        List<PriceRecord> prices = d.getPrices(securitySym, DateUtil.getDate(2001,1,1), new Date());
                        computeFreq(results.getCondition().getTestScaling(), securitySym, prices, results.getCondition(), occuranceFreqMap);
                    } catch (Exception e) {
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

        DBCollection patternCollection = db.getCollection("patterns");
        patternCollection.ensureIndex("pattern");
        patternCollection.ensureIndex("score");
        int i = 0;
        int total = occuranceFreqMap.size();
        for(Map.Entry<String,PatternInstance> entry : occuranceFreqMap.entrySet())
        {
            i++;
            System.out.println(i+"/"+total+" --> "+(i*100/total));
            PatternDAO.create(db, "patterns", entry.getValue());
        }

        /*Downloader d = new YahooDownloader();
        List<PriceRecord> prices = d.getPrices("WAG", DateUtil.getDate(2001,1,1), new Date());
        Collections.sort(prices);

        PointFigureGraph pfg = new PointFigureGraph(prices, results.getCondition().getTestScaling());

        for(PriceRecord pr : prices)
        {
            System.out.println(pr.getDateCode()+","+pfg.getColumnDateMapTwo());
        }*/

        List<PresentScore> s = new ArrayList<PresentScore>();
        /*for(Map.Entry<String,PatternInstance> e : occuranceFreqMap.entrySet())
        {
            s.add(new PresentScore(e.getKey(), e.getValue()));
        }
        Collections.sort(s);
        for(PresentScore a : s)
        {
            System.out.println(a.getSymbol()+": "+a.getPi().getScore()+" -- "+a.getPi());
        }*/


        String sym = "WAG";
    }
    /*
    private void getTopFivePatterns(Map<String, PatternInstance> occuranceFreqMap)
    {
        Double topScore = new Double(0);
        String topPattern = "";
        for(PatternInstance pi : occuranceFreqMap.values())
        {
            if(pi.getScore()>topScore)
            {
                topScore = pi.getScore();
                topPattern = pi.getPattern();
            }
        }

        int i = 0;
    }

    public List<String> getSymbolsFromFile()
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
    }   */

    private void computeFreq(Scaling s, String symbol, List<PriceRecord> prices, Condition testCondition, Map<String, PatternInstance> occuranceFreqMap)
    {
        Collections.sort(prices);
        List<PriceRecord> pricesSoFar = new ArrayList<PriceRecord>();
        Map<String,Set<Integer>> patternLocations = new HashMap<String,Set<Integer>>();

        PointFigureGraph pfg = new PointFigureGraph(prices, s);

        for(int prIdx = 0; prIdx<prices.size(); prIdx++)
        {
            List<PFColumn> curCols = pfg.getColumnDateMap().get(prices.get(prIdx).getDateCode());
            if(curCols.size()<testCondition.getMinPatternSize()+2) continue;

            int maxPatternSize = Math.min(testCondition.getMaxPatternSize(), curCols.size() - testCondition.getMinPatternSize());
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
                String patternCode = PFColumnUtil.getPattern(curCols, patternSize);

                //Add pattern location if we've never seen it before
                if(patternLocations.get(patternCode)==null)
                {
                    HashSet<Integer> seenLocations = new HashSet<Integer>();
                    patternLocations.put(patternCode,seenLocations);
                }

                if(!patternLocations.get(patternCode).contains(curCols.size()))
                {
                    PatternInstance of = new PatternInstance();
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
}
