package com.purdynet.scaling.impl;

import com.purdynet.scaling.Scaling;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/27/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractScaling implements Scaling
{
    protected List<BigDecimal> values;

    @Override
    public List<BigDecimal> getValues() {
        return values;
    }

    @Override
    public Integer getIdx(BigDecimal searchValue)
    {
        return getIdxByBTree(searchValue);
    }

    public Integer getIdxByLoop(BigDecimal searchValue)
    {
        if(searchValue.compareTo(values.get(0)) < 0) return -1;
        for(int i=1; i<values.size(); i++)
        {
            if(values.get(i-1).compareTo(searchValue) <= 0 && values.get(i).compareTo(searchValue) > 0) return i;
        }
        return values.size();
    }

    public Integer getIdxByBTree(BigDecimal price)
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
