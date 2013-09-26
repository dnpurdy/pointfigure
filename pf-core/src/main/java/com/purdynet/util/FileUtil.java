package com.purdynet.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/22/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil
{
    public static List<String> getSymbolsFromFile(String filename)
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
