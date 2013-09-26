package com.purdynet.app;

import com.purdynet.pattern.PatternInstance;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/11/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatternScores implements Comparable<PatternScores>
{
    private String pattern;
    private PatternInstance pi;

    public PatternScores(String pattern, PatternInstance pi)
    {
        this.pattern = pattern;
        this.pi = pi;
    }

    public PatternInstance getPi() {
        return pi;
    }

    @Override
    public int compareTo(PatternScores o)
    {
        return this.getPi().getScore().compareTo(o.getPi().getScore());
    }
}
