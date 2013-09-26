package com.purdynet.app;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.purdynet.condition.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.graph.PointFigureGraph;
import com.purdynet.persistence.MongoUtils;
import com.purdynet.persistence.PatternDAO;
import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.impl.PercentScaling;
import com.purdynet.util.DateUtil;
import com.purdynet.util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        List<String> symbols = FileUtil.getSymbolsFromFile(filename);

        final Condition tCond = new Condition(new PercentScaling(0.01), 25, 120, 2, 15);

        MongoClient mongoClient = MongoUtils.getClient();
        DB db = mongoClient.getDB("1379378527650");

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
                PointFigureGraph pfg = new PointFigureGraph(prices, tCond.getTestScaling());

                PresentScore ps = null;
                for(int i = tCond.getMinPatternSize(); i<=tCond.getMaxPatternSize(); i++)
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
            System.out.println(sym.getSymbol()+": "+sym.getPi().getScore()+" -- "+sym.getPi());
        }
    }
}
