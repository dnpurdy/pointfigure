package com.purdynet.scaling.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/27/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PercentScaling extends AbstractScaling
{
    static final long serialVersionUID = 201311232009010002L;

    private Double percent;

    public Double getPercent() {
        return percent;
    }

    public PercentScaling(Double percent)
    {
        this(new BigDecimal(1), new BigDecimal(100000) ,percent);
    }

    public PercentScaling(BigDecimal initialValue, BigDecimal terminalValue, Double percent)
    {
        this.percent = percent;

        this.values = new ArrayList<BigDecimal>();
        values.add(new BigDecimal(0));
        values.add(initialValue);

        while(values.get(values.size()-1).compareTo(terminalValue) < 0)
        {
            BigDecimal lastVal = values.get(values.size() - 1);
            BigDecimal nextVal = lastVal.multiply(new BigDecimal(1).add(new BigDecimal(percent)), MathContext.DECIMAL32);
            values.add(nextVal);
        }

        Collections.sort(values);
    }
}
