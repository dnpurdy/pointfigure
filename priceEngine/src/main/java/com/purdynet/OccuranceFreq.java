package com.purdynet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/17/13
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OccuranceFreq implements Comparable<OccuranceFreq>
{
    private String pattern;
    private double timesHappened = 0;
    private double timesGood = 0;
    private Set<String> symbolsHappened = new HashSet<String>();
    private Set<String> symbolsGood = new HashSet<String>();

    public OccuranceFreq(String pattern)
    {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public Set<String> getSymbolsHappened() {
        return symbolsHappened;
    }

    public Set<String> getSymbolsGood() {
        return symbolsGood;
    }

    public String symbolsHappenedPP() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String sym : symbolsHappened)
        {
            sb.append(sym).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public String symbolsGoodPP() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String sym : symbolsGood)
        {
            sb.append(sym).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void happenedNoGood(String sym)
    {
        timesHappened++;
        symbolsHappened.add(sym);
    }

    public void happenedGood(String sym)
    {
        happenedNoGood(sym);
        timesGood++;
        symbolsGood.add(sym);
    }

    public Integer percentGood()
    {
        if(timesHappened == 0) return 0;
        else
        {
            return (new BigDecimal(timesGood*100).divide(new BigDecimal(timesHappened), 8, RoundingMode.HALF_EVEN)).intValue();
        }
    }

    public Integer happened() {
        return new BigDecimal(timesHappened).intValue();
    }

    public Integer score() {
        double dem = Math.pow(2,timesHappened);
        double num = dem - 1;
        double factor = 1/(Math.log(timesHappened)/Math.log(2));
        double freq = timesGood/timesHappened;
        if(timesHappened==1) return new Integer(0);
        else return new BigDecimal(100*((1-factor)*(freq))).intValue();
    }

    @Override
    public int compareTo(OccuranceFreq o) {
        return o.score().compareTo(this.score());
    }

}
