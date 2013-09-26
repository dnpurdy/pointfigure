package com.purdynet.condition;

import com.purdynet.scaling.Scaling;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/31/13
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Condition
{
    static final long serialVersionUID = 201308232153001L;

    private Scaling testScaling;
    private int percentReturn;
    private int daysHorizon;
    private int minPatternSize;
    private int maxPatternSize;

    public Condition(Scaling testScaling, int percentReturn, int daysHorizon, int minPatternSize, int maxPatternSize)
    {
        this.testScaling = testScaling;
        this.percentReturn = percentReturn;
        this.daysHorizon = daysHorizon;
        this.minPatternSize = minPatternSize;
        this.maxPatternSize = maxPatternSize;
    }

    public Scaling getTestScaling() {
        return testScaling;
    }

    public int getPercentReturn() {
        return percentReturn;
    }

    public int getDaysHorizon() {
        return daysHorizon;
    }

    public int getMinPatternSize() {
        return minPatternSize;
    }

    public int getMaxPatternSize() {
        return maxPatternSize;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "testScaling=" + testScaling +
                ", percentReturn=" + percentReturn +
                ", daysHorizon=" + daysHorizon +
                ", minPatternSize=" + minPatternSize +
                ", maxPatternSize=" + maxPatternSize +
                '}';
    }
}