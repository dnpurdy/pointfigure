package com.purdynet.scaling;

import org.apache.commons.lang3.time.StopWatch;
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
public class LogScalingTest
{
    @Test
    public void testPercentScalingStart()
    {
        LogScaling ps = new LogScaling(0.01);
        List<BigDecimal> values = ps.getValues();
        //assertEquals(new BigDecimal(0), values.get(0));
    }

    @Test
    public void testGetIdx()
    {
        LogScaling ps = new LogScaling(0.01);
        assertEquals(244L, ps.getIdx(new BigDecimal(284)).longValue());
    }

    @Test
    public void testLoopGetIdx()
    {
        PercentScaling ps = new PercentScaling(0.01);
        StopWatch sw = new StopWatch();

        sw.start();
        for(int i=1; i<2000; i++)
        {
            ps.getIdxByLoop(new BigDecimal(i));
        }
        sw.stop();
        System.out.println(sw.getNanoTime());
    }

    @Test
    public void testBTreeGetIdx()
    {
        PercentScaling ps = new PercentScaling(0.01);
        StopWatch sw = new StopWatch();

        sw.start();
        for(int i=1; i<2000; i++)
        {
            ps.getIdxByBTree(new BigDecimal(i));
        }
        sw.stop();
        System.out.println(sw.getNanoTime());
    }
}
