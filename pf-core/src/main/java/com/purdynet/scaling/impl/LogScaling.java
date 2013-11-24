package com.purdynet.scaling.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/27/13
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogScaling extends AbstractScaling
{
    static final long serialVersionUID = 201311232009010001L;

    private double factor;

    public LogScaling(double factor)
    {
        this.factor = factor;
        this.values = new ArrayList<BigDecimal>();
        BigDecimal i =  new BigDecimal("1");
        while(i.compareTo(new BigDecimal(100000)) < 0)
        {
            i = new BigDecimal(Math.pow(10,Math.log10(i.doubleValue())+factor));
            values.add(i);
        }
    }
}
