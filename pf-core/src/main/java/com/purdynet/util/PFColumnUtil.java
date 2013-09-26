package com.purdynet.util;

import com.purdynet.graph.PFColumn;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/31/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PFColumnUtil
{
    public static String getPattern(List<PFColumn> columns, Integer patternSize)
    {
        if(patternSize>columns.size()) return null;
        else
        {
            StringBuilder sb = new StringBuilder();
            for(int i = columns.size()-1-patternSize; i<columns.size(); i++)
            {
                sb.append(columns.get(i).toString());
                if(i!=columns.size()-1) sb.append("-");
            }
            return sb.toString();
        }
    }
}
