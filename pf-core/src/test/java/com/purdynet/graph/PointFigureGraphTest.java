package com.purdynet.graph;

import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.impl.LogScaling;
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
        PointFigureGraph pfg = new PointFigureGraph(getTestPrices(), TraditionalScaling.scale());

        assertEquals("1X", pfg.getPattern("2013-01-01"));
        assertEquals("3X", pfg.getPattern("2013-01-02"));
        assertEquals("3X-3O", pfg.getPattern("2013-01-03"));
        assertEquals("3X-3O-4X", pfg.getPattern("2013-01-04"));
        assertEquals("3X-3O-5X", pfg.getPattern("2013-01-05"));
        assertEquals("3X-3O-5X-3O", pfg.getPattern("2013-01-06"));
    }

    private List<PriceRecord> getTestPrices() {
        List<PriceRecord> testPrices = new ArrayList<>();
        testPrices.add(new PriceRecord("2013-01-01", new scala.math.BigDecimal(new BigDecimal(21.1))));
        testPrices.add(new PriceRecord("2013-01-02", new scala.math.BigDecimal(new BigDecimal(23.1))));
        testPrices.add(new PriceRecord("2013-01-03", new scala.math.BigDecimal(new BigDecimal(20.6))));
        testPrices.add(new PriceRecord("2013-01-04", new scala.math.BigDecimal(new BigDecimal(24.6))));
        testPrices.add(new PriceRecord("2013-01-05", new scala.math.BigDecimal(new BigDecimal(25.6))));
        testPrices.add(new PriceRecord("2013-01-06", new scala.math.BigDecimal(new BigDecimal(22.6))));
        return testPrices;
    }

    @Test
    public void getScalingValues() {
        PointFigureGraph pfg1 = new PointFigureGraph(getTestPrices(), TraditionalScaling.scale());
        assertEquals("Scaling should be equal!", TraditionalScaling.scale().values(), pfg1.getScalingValues());

        PointFigureGraph pfg2 = new PointFigureGraph(getTestPrices(), LogScaling.ONE_FIVE());
        assertEquals("Scaling should be equal!", LogScaling.ONE_FIVE().values(), pfg2.getScalingValues());
    }

    @Test
    public void getCurCols() {
        PointFigureGraph pfg1 = new PointFigureGraph(getTestPrices(), TraditionalScaling.scale());
        assertEquals("curCols should be equal!", "[1X]", pfg1.getCurCols("2013-01-01").toString());
        assertEquals("curCols should be equal!", "[3X]", pfg1.getCurCols("2013-01-02").toString());
        assertEquals("curCols should be equal!", "[3X, 3O]", pfg1.getCurCols("2013-01-03").toString());
        assertEquals("curCols should be equal!", "[3X, 3O, 4X]", pfg1.getCurCols("2013-01-04").toString());
        assertEquals("curCols should be equal!", "[3X, 3O, 5X]", pfg1.getCurCols("2013-01-05").toString());
        assertEquals("curCols should be equal!", "[3X, 3O, 5X, 3O]", pfg1.getCurCols("2013-01-06").toString());

        PointFigureGraph pfg2 = new PointFigureGraph(getTestPrices(), TraditionalScaling.scale(), 4);
        assertEquals("curCols should be equal!", "[1X]", pfg2.getCurCols("2013-01-01").toString());
        assertEquals("curCols should be equal!", "[3X]", pfg2.getCurCols("2013-01-02").toString());
        assertEquals("curCols should be equal!", "[3X]", pfg2.getCurCols("2013-01-03").toString());
        assertEquals("curCols should be equal!", "[4X]", pfg2.getCurCols("2013-01-04").toString());
        assertEquals("curCols should be equal!", "[5X]", pfg2.getCurCols("2013-01-05").toString());
        assertEquals("curCols should be equal!", "[5X]", pfg2.getCurCols("2013-01-06").toString());
    }

    @Test
    public void getColumnDateMap() {
    }

    @Test
    public void getColumns() {
    }

    @Test
    public void getPattern() {
    }

    @Test
    public void getPattern1() {
    }

    @Test
    public void getPattern2() {
    }


}
