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
        testPrices.add(new PriceRecord("2013-01-01", new BigDecimal(5.1)));

        PointFigureGraph pfg = new PointFigureGraph(testPrices, new TraditionalScaling());
        assertEquals(1, pfg.getColumns().size());

        testPrices.add(new PriceRecord("2013-01-02", new BigDecimal(10.1)));
        pfg = new PointFigureGraph(testPrices, new TraditionalScaling());
        assertEquals(1, pfg.getColumns().size());
    }
}
