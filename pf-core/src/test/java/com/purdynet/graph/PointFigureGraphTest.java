package com.purdynet.graph;

import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.impl.TraditionalScaling;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/28/13
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointFigureGraphTest
{
    @Test
    public void testSimpleGraph()
    {
        List<PriceRecord> testPrices = new ArrayList<PriceRecord>();
        testPrices.add(new PriceRecord("2013-01-01", new BigDecimal(21.1)));
        testPrices.add(new PriceRecord("2013-01-02", new BigDecimal(23.1)));
        testPrices.add(new PriceRecord("2013-01-03", new BigDecimal(20.6)));
        testPrices.add(new PriceRecord("2013-01-04", new BigDecimal(24.6)));
        testPrices.add(new PriceRecord("2013-01-05", new BigDecimal(25.6)));
        testPrices.add(new PriceRecord("2013-01-06", new BigDecimal(22.6)));

        PointFigureGraph pfg = new PointFigureGraph(testPrices, new TraditionalScaling());

        assertEquals("1X", pfg.getPattern("2013-01-01"));
        assertEquals("3X", pfg.getPattern("2013-01-02"));
        assertEquals("3X-3O", pfg.getPattern("2013-01-03"));
        assertEquals("3X-3O-4X", pfg.getPattern("2013-01-04"));
        assertEquals("3X-3O-5X", pfg.getPattern("2013-01-05"));
        assertEquals("3X-3O-5X-3O", pfg.getPattern("2013-01-06"));
    }
}
