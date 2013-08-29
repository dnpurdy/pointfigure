package com.purdynet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/23/13
 * Time: 1:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class Pattern implements Serializable
{
    static final long serialVersionUID = 201308230118001L;

    private String pattern;
    private Integer timesSeen = new Integer(0);
    private Set<String> symbolsSeen = new HashSet<String>();
    private Integer timesSuccessful = new Integer(0);
    private Set<String> symbolSuccessful = new HashSet<String>();

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getTimesSeen() {
        return timesSeen;
    }

    public void setTimesSeen(Integer timesSeen) {
        this.timesSeen = timesSeen;
    }

    public Set<String> getSymbolsSeen() {
        return symbolsSeen;
    }

    public void setSymbolsSeen(Set<String> symbolsSeen) {
        this.symbolsSeen = symbolsSeen;
    }

    public void addSymbolSeen(String symbolSeen) {
        symbolsSeen.add(symbolSeen);
    }

    public Integer getTimesSuccessful() {
        return timesSuccessful;
    }

    public void setTimesSuccessful(Integer timesSuccessful) {
        this.timesSuccessful = timesSuccessful;
    }

    public Set<String> getSymbolSuccessful() {
        return symbolSuccessful;
    }

    public void setSymbolSuccessful(Set<String> symbolSuccessful) {
        this.symbolSuccessful = symbolSuccessful;
    }

    public void addSymbolSuccessful(String sym) {
        symbolSuccessful.add(sym);
    }

    public Double getScore()
    {
        double factor = 1/(Math.log(timesSeen)/Math.log(2));
        double freq = new BigDecimal(timesSuccessful).divide(new BigDecimal(timesSeen),8, RoundingMode.HALF_EVEN).doubleValue();
        if(timesSeen==1) return new Double(0);
        else return new BigDecimal(numDash(pattern)*100*((1-factor)*(freq))).doubleValue();
    }

    private Integer numDash(String pattern)
    {
        return numDash(pattern, 0);
    }

    private Integer numDash(String pattern, Integer accu)
    {
        if(pattern.contains("-")) return numDash(pattern.replaceFirst("-", ""), accu+1);
        else return accu;
    }
}
