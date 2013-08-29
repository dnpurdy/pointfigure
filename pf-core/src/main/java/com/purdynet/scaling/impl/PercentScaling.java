package com.purdynet.scaling.impl;

import com.purdynet.scaling.Scaling;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/17/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PercentScaling implements Scaling, Serializable
{
    static final long serialVersionUID = 201308230110001L;

    private double percent;
    private List<BigDecimal> values;

    public PercentScaling(double percent)
    {
        this.percent = percent;

        this.values = new ArrayList<BigDecimal>();

        double factor = 1+percent;
        BigDecimal i = new BigDecimal(0);
        values.add(i);
        i = new BigDecimal(1);
        values.add(i);
        i = new BigDecimal(2);
        while ( i.compareTo(new BigDecimal(100000)) < 0 )
        {
            values.add(i);
            double j = i.doubleValue();
            i = new BigDecimal(j*factor);
        }

    }

    @Override
    public Integer getIdx(BigDecimal price) {
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

    @Override
    public List<BigDecimal> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "PercentScaling{" +
                "percent=" + percent +
                '}';
    }
}
