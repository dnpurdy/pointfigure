package com.purdynet;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.graph.PointFigureGraph;
import com.purdynet.prices.PriceRecord;
import com.purdynet.survey.SurveyResult;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/23/13
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatternReader
{
    private static PatternContext results;

    public static void main(String[] args) throws Exception
    {
        PatternReader pe = new PatternReader();
        pe.run(args);
    }

    public void run(String[] args)
    {
        //results = PatternContext.readFromFile("quarks.ser");
        System.out.println(PatternContext.describe(results));

        //List<String> symbols = readSymbolFile("/home/dnpurdy/Desktop/spsym.csv");
        List<String> symbols = Arrays.asList("GE");
        List<SurveyResult> scores = new ArrayList<SurveyResult>();

        int i = 1;
        //List<String> symbols = Arrays.asList("APH","WAG");
        for(String sym : symbols)
        {
            try
            {
                Downloader d = new YahooDownloader();
                Calendar c = Calendar.getInstance();
                c.set(2012, 0, 1);
                Date startDate = c.getTime();
                File dl = d.download(sym, startDate, new Date());
                File dest = new File("/home/dnpurdy/pf/"+sym+".csv");
                FileUtils.copyFile(dl, dest);

                List<PriceRecord> prices = parseFile(new File("/home/dnpurdy/pf/" + sym + ".csv"));

                PointFigure pf = new PointFigure(prices, results.getCondition().getTestScaling());
                PointFigureGraph pfg = new PointFigureGraph(prices, results.getCondition().getTestScaling());

                SurveyResult sr = new SurveyResult("", new Pattern(), 0.0);

                for(int patternSize = results.getCondition().getMinPatternSize();
                    patternSize <= Math.min(results.getCondition().getMaxPatternSize(), pf.getNumberOfColumns());
                    patternSize++)
                {
                    String patternCode = pf.getPatternCode(patternSize);
                    if(results.getPatterns().containsKey(patternCode))
                    {
                        Double curScore = results.getPatterns().get(patternCode).getScore();

                        if(curScore!=null && curScore > sr.getScore())
                        {
                            sr = new SurveyResult(sym, results.getPatterns().get(patternCode), curScore);
                        }
                    }
                }

                scores.add(sr);
                i++;
                System.out.println(i);
            }
            catch(IOException e)
            {
            }
        }

        Collections.sort(scores);
        for(SurveyResult sr : scores)
        {
            System.out.println(sr.getSymbol()+": "+sr.getBestPattern().getPattern()+" ("+sr.getScore()+")");
            System.out.println(" "+sr.getBestPattern().getSymbolSuccessful().size()+" symbols won/"+sr.getBestPattern().getSymbolsSeen().size());
            System.out.println(" "+sr.getBestPattern().getTimesSuccessful()+" times won/"+sr.getBestPattern().getTimesSeen());
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
}

