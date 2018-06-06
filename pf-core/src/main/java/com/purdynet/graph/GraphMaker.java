package com.purdynet.graph;

import au.com.bytecode.opencsv.CSVReader;
import com.purdynet.data.Downloader;
import com.purdynet.data.impl.YahooDownloader;
import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.Scaling;
import com.purdynet.scaling.impl.PercentScaling;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/28/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphMaker
{
    public static void main(String[] args) throws Exception
    {
        GraphMaker pe = new GraphMaker();
        pe.run(args);
    }

    public void run(String[] args)
    {
        List<String> symbols = Arrays.asList("APH");
        for(String sym : symbols)
        {
            try
            {
                Downloader d = new YahooDownloader();
                Calendar c = Calendar.getInstance();
                c.set(2001, 0, 1);
                Date startDate = c.getTime();
                File dl = d.download(sym, startDate, new Date());
                File dest = new File("/home/dnpurdy/pf/"+sym+".csv");
                FileUtils.copyFile(dl, dest);

                List<PriceRecord> prices = parseFile(new File("/home/dnpurdy/pf/" + sym + ".csv"));

                PointFigureGraph pfg = new PointFigureGraph(prices, new PercentScaling(0.01));
                System.out.println(formatCols(pfg.getColumnDateMap().get("2013-08-28"), pfg.getScaling()));
                pfg.render();
            }
            catch(IOException e)
            {}
        }
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

    private String formatCols(List<PFColumn> colList, Scaling s)
    {
        StringBuilder sb = new StringBuilder();
        for(PFColumn col : colList)
        {
            sb.append(col.getNumber().toString());
            sb.append(col.getColumnType().name());
            //sb.append("(");
            //sb.append(s.getValues().get(col.getLowBoxIdx()));
            //sb.append(":");
            //sb.append(s.getValues().get(col.getHighBoxIdx()));
            //sb.append(")");
            sb.append("-");
        }
        return sb.toString();
    }
}
