package com.purdynet.app;

import com.purdynet.pattern.PatternInstance;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/2/13
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class PresentScore implements Comparable<PresentScore>
{
    private String symbol;
    private PatternInstance pi;
    private Double score;

    public PresentScore(String symbol, PatternInstance pi)
    {
        this.symbol = symbol;
        this.pi = pi;
        this.score = pi.getScore();
    }

    public PresentScore(String symbol, Double score)
    {
        this.symbol = symbol;
        this.pi = null;
        this.score = score;
    }

    public String getSymbol() {
        return symbol;
    }

    public PatternInstance getPi() {
        return pi;
    }

    public Double getScore() {
        return score;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPi(PatternInstance pi) {
        this.pi = pi;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(PresentScore o)
    {
        return getScore().compareTo(o.getScore());
    }
}
