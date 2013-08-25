package com.purdynet.scaling.impl;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/17/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PercentScalingTest
{
    @Test
    public void testPercentScalingStart()
    {
        PercentScaling ps = new PercentScaling(0.01);
        List<BigDecimal> values = ps.getValues();
        assertEquals(new BigDecimal(0), values.get(0));
    }

    @Test
    public void testGetIdx()
    {
        PercentScaling ps = new PercentScaling(0.01);
        assertEquals(500L, ps.getIdx(new BigDecimal(284)).longValue());
    }
}
