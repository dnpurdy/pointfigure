package com.purdynet.app;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.purdynet.condition.Condition;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.graph.PFColumn;
import com.purdynet.graph.PointFigureGraph;
import com.purdynet.persistence.MongoUtils;
import com.purdynet.persistence.PatternDAO;
import com.purdynet.prices.PriceRecord;
import com.purdynet.prices.ScoreRecord;
import com.purdynet.scaling.impl.PercentScaling;
import com.purdynet.util.DateUtil;
import com.purdynet.util.PFColumnUtil;

import java.io.BufferedReader;
import java.io.FileReader;
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
public class SymbolHistory
{
    private String filename;

    public static void main(String[] args) throws Exception
    {
        SymbolHistory sh = new SymbolHistory();
        sh.run();
    }

    public void parseArgs(String[] args)
    {
        if(args.length==0 || args.length>1) System.exit(1);
        this.filename = args[0];
    }

    public void run()
    {
//        List<String> symbols = getSymbolsFromFile();
        final Condition tCond = new Condition(new PercentScaling(0.01), 25, 120, 2, 15);

        MongoClient mongoClient = MongoUtils.getClient();
        DB db = mongoClient.getDB("1379378527650");

        //symbols = getSymbolsFromFile();
        String symbol = "T";

        Downloader d = new YahooDownloader();
        List<PriceRecord> prices = d.getPrices(symbol, DateUtil.getDate(2001, 1, 1), new Date());
        PointFigureGraph pfg = new PointFigureGraph(prices, tCond.getTestScaling());

        List<ScoreRecord> scoreRecords = new ArrayList<ScoreRecord>();
        for(String dateCode : pfg.getColumnDateMap().keySet())
        {
            List<PFColumn> curCols = pfg.getColumnDateMap().get(dateCode);
            if(curCols.size()<tCond.getMinPatternSize()+2) continue;

            int maxPatternSize = Math.min(tCond.getMaxPatternSize(), curCols.size() - tCond.getMinPatternSize());
            double score = 0.0;
            for(int patternSize = tCond.getMinPatternSize(); patternSize <= maxPatternSize; patternSize++)
            {
                String patternCode = PFColumnUtil.getPattern(curCols, patternSize);
                double curScore = PatternDAO.getScore(db, "patterns", patternCode);
                if(curScore>score) score = curScore;
            }
            scoreRecords.add(new ScoreRecord(dateCode, score));
        }

        Collections.sort(scoreRecords);
        for(ScoreRecord sr : scoreRecords)
        {
            System.out.println(sr);
        }

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
    }

}
