package com.purdynet.scaling.impl;

import com.purdynet.scaling.Scaling;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/16/13
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogScaling implements Scaling
{

    private double factor;
    private List<BigDecimal> values;

    public LogScaling(double factor)
    {
        this.factor = factor;
        this.values = new ArrayList<BigDecimal>();
        BigDecimal i =  new BigDecimal("1");
        while(i.compareTo(new BigDecimal(2000)) < 0)
        {
            i = new BigDecimal(Math.pow(10,Math.log10(i.doubleValue())+factor));
            values.add(i);
        }
    }

    @Override
    public List<BigDecimal> getValues()
    {
        return values;
    }

    @Override
    public Integer getIdx(BigDecimal price)
    {
        int lowIdx = 0;
        int highIdx = values.size();
        int midIdx = (highIdx - lowIdx) / 2 + lowIdx;

        while(lowIdx != midIdx && midIdx != highIdx)
        {
            BigDecimal lowVal = values.get(lowIdx);
            BigDecimal midVal = values.get(midIdx);

            if(price.compareTo(lowVal)>=0 && price.compareTo(midVal)<0)
            {
                highIdx = midIdx;
                midIdx = (highIdx - lowIdx) / 2 + lowIdx;
            }
            else
            {
                lowIdx = midIdx;
                midIdx = (highIdx - lowIdx) / 2 + lowIdx;
            }
        }
        return lowIdx;
    }
}
