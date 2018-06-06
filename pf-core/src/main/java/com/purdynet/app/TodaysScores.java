package com.purdynet.app;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.purdynet.condition.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.graph.PointFigureGraph;
import com.purdynet.persistence.ConditionDAO;
import com.purdynet.persistence.MongoUtils;
import com.purdynet.persistence.PatternDAO;
import com.purdynet.prices.PriceRecord;
import com.purdynet.util.DateUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/16/13
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodaysScores
{
    private String filename;

    public static void main(String[] args) throws Exception
    {
        TodaysScores sh = new TodaysScores();
        sh.parseArgs(args);
        sh.run();
    }

    public void parseArgs(String[] args)
    {
        if(args.length==0 || args.length>1) System.exit(1);
        this.filename = args[0];
    }

    public void run()
    {
        //List<String> symbols = FileUtil.getSymbolsFromFile(filename);
        List<String> symbols = Arrays.asList("GE");



        MongoClient mongoClient = MongoUtils.getClient("localhost");
        DB db = mongoClient.getDB("1385259305852");
        final Condition tCond = ConditionDAO.getCondition(db, "condition");
        int size = symbols.size();
        int count = 0;
        List<PresentScore> todaysScores = new ArrayList<PresentScore>();
        for(final String securitySym : symbols)
        {
            count++;
            System.out.println(count+"/"+size);
            try {
                Downloader d = new YahooDownloader();
                List<PriceRecord> prices = d.getPrices(securitySym, DateUtil.getDate(2001,1,1), new Date());
                PointFigureGraph pfg = new PointFigureGraph(prices, tCond.testScaling());

                PresentScore ps = null;
                for(int i = tCond.minPatternSize(); i<=tCond.maxPatternSize(); i++)
                {
                    if(ps==null)
                    {
                        ps = new PresentScore(securitySym, PatternDAO.getScore(db, "patterns", pfg.getPattern(i)));
                    }
                    else
                    {
                        Double s = PatternDAO.getScore(db, "patterns", pfg.getPattern(i));

                        if(s > ps.getScore())
                        {
                            ps = new PresentScore(securitySym, s);
                        }

                    }
                }
                if(ps!=null) todaysScores.add(ps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(todaysScores);
        for(PresentScore sym : todaysScores)
        {
            System.out.println(sym.getSymbol()+": "+sym.getScore()+" -- "+sym.getPi());
        }
    }
}
