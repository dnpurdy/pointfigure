package com.purdynet;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import org.apache.commons.io.FileUtils;

import java.io.*;
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
        results = PatternContext.readFromFile("quarks.ser");
        System.out.println(PatternContext.describe(results));

        List<String> symbols = readSymbolFile("/home/dnpurdy/Desktop/spsym.csv");

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

                Double score = new Double(0);
                String pattern = "";
                for(int patternSize = results.getCondition().getMinPatternSize();
                    patternSize <= results.getCondition().getMaxPatternSize();
                    patternSize++)
                {
                    String patternCode = pf.getPatternCode(patternSize);
                    Double curScore = results.getScores().get(patternCode);
                    if(curScore!=null && curScore > score)
                    {
                        score = curScore;
                        pattern = patternCode;
                    }
                }

                System.out.println(sym+": "+pattern+" => "+score);
            }
            catch(IOException e)
            {
            }
        }

        Double maxScore = new Double(0);
        for(Map.Entry<String,Double> entry : results.getScores().entrySet())
        {
            if(entry.getValue() > maxScore )
            {
                System.out.println(entry.getKey()+": "+entry.getValue().toString());
                maxScore = entry.getValue();
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

