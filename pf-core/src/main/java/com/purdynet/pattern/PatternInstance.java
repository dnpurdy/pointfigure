package com.purdynet.pattern;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/31/13
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatternInstance implements Serializable
{
    static final long serialVersionUID = 201308300118001L;

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
        return (timesSuccessful*(Math.log(timesSeen+1)/Math.log(2)))/timesSeen;
        //return timesSuccessful.doubleValue();
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

    @Override
    public String toString() {
        return "PatternInstance{" +
                "pattern='" + pattern + '\'' +
                ", timesSeen=" + timesSeen +
                ", timesSuccessful=" + timesSuccessful +
                ", symbolsSeen=" + symbolsSeen +
                ", symbolSuccessful=" + symbolSuccessful +
                '}';
    }
}

