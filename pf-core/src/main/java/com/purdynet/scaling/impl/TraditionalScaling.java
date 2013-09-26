package com.purdynet.scaling.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/31/13
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class TraditionalScaling extends AbstractScaling
{
    public TraditionalScaling()
    {
        BigDecimal i = new BigDecimal(0);
        BigDecimal inc = new BigDecimal(0.625);
        values = new ArrayList<BigDecimal>();

        while(i.compareTo(new BigDecimal(100000)) < 0)
        {
            values.add(i);
            if(i.compareTo(new BigDecimal(0)) >= 0 && i.compareTo(new BigDecimal(0.25)) <= 0) inc = new BigDecimal(0.625);
            if(i.compareTo(new BigDecimal(0.25)) >= 0 && i.compareTo(new BigDecimal(1)) <= 0) inc = new BigDecimal(0.125);
            if(i.compareTo(new BigDecimal(1)) >= 0 && i.compareTo(new BigDecimal(5)) <= 0) inc = new BigDecimal(0.25);
            if(i.compareTo(new BigDecimal(5)) >= 0 && i.compareTo(new BigDecimal(20)) <= 0) inc = new BigDecimal(0.5);
            if(i.compareTo(new BigDecimal(20)) >= 0 && i.compareTo(new BigDecimal(100)) <= 0) inc = new BigDecimal(1);
            if(i.compareTo(new BigDecimal(100)) >= 0 && i.compareTo(new BigDecimal(200)) <= 0) inc = new BigDecimal(2);
            if(i.compareTo(new BigDecimal(200)) >= 0 && i.compareTo(new BigDecimal(500)) <= 0) inc = new BigDecimal(4);
            if(i.compareTo(new BigDecimal(500)) >= 0 && i.compareTo(new BigDecimal(1000)) <= 0) inc = new BigDecimal(5);
            if(i.compareTo(new BigDecimal(1000)) >= 0 && i.compareTo(new BigDecimal(25000)) <= 0) inc = new BigDecimal(50);
            if(i.compareTo(new BigDecimal(25000)) >= 0) inc = new BigDecimal(500);
            i = i.add(inc);
        }
    }

}
