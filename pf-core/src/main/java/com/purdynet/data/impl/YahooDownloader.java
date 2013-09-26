package com.purdynet.data.impl;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.data.Downloader;
import com.purdynet.prices.PriceRecord;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/17/13
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class YahooDownloader implements Downloader
{
    @Override
    public File download(String symbol, Date start, Date end)
    {
        BufferedReader in = null;
        BufferedWriter out = null;

        try
        {
            //Start url with symbol
            StringBuilder url = new StringBuilder("http://ichart.finance.yahoo.com/table.csv?s=");
            url.append(symbol);

            //handle start date
            final Calendar c = Calendar.getInstance();
            c.setTime(start);
            url.append("&a=");
            url.append(c.get(Calendar.MONTH));
            url.append("&b=");
            url.append(c.get(Calendar.DAY_OF_MONTH));
            url.append("&c=");
            url.append(c.get(Calendar.YEAR));

            //handle end date
            c.setTime(end);
            url.append("&d=");
            url.append(c.get(Calendar.MONTH));
            url.append("&e=");
            url.append(c.get(Calendar.DAY_OF_MONTH));
            url.append("&f=");
            url.append(c.get(Calendar.YEAR));

            //handle static portion
            //url.append("&ignore=.csv");

            URL oracle = new URL(url.toString());
            in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            out = new BufferedWriter(new FileWriter(new File("/tmp/"+symbol+".csv")));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                out.write(inputLine);
                out.write("\n");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                if(in!=null) in.close();
                if(out!=null) out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return new File("/tmp/"+symbol+".csv");
    }

    @Override
    public List<PriceRecord> getPrices(String symbol, Date start, Date end)
    {
        List<PriceRecord> ret = new ArrayList<PriceRecord>();
        CSVReader reader = null;
        try
        {
            //Start url with symbol
            StringBuilder url = new StringBuilder("http://ichart.finance.yahoo.com/table.csv?s=");
            url.append(symbol);

            //handle start date
            final Calendar c = Calendar.getInstance();
            c.setTime(start);
            url.append("&a=");
            url.append(c.get(Calendar.MONTH));
            url.append("&b=");
            url.append(c.get(Calendar.DAY_OF_MONTH));
            url.append("&c=");
            url.append(c.get(Calendar.YEAR));

            //handle end date
            c.setTime(end);
            url.append("&d=");
            url.append(c.get(Calendar.MONTH));
            url.append("&e=");
            url.append(c.get(Calendar.DAY_OF_MONTH));
            url.append("&f=");
            url.append(c.get(Calendar.YEAR));

            //handle static portion
            //url.append("&ignore=.csv");

            URL oracle = new URL(url.toString());
            reader = new CSVReader(new InputStreamReader(oracle.openStream()));
            String[] line;
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
            try {
                if(reader!=null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return ret;
    }
}
