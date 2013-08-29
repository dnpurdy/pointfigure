package com.purdynet.survey;

import com.purdynet.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/24/13
 * Time: 10:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyResult implements Comparable<SurveyResult>
{
    private String symbol;
    private Pattern bestPattern;
    private Double score;

    public SurveyResult(String symbol, Pattern bestPattern, Double score)
    {
        this.symbol = symbol;
        this.bestPattern = bestPattern;
        this.score = score;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Pattern getBestPattern() {
        return bestPattern;
    }

    public void setBestPattern(Pattern bestPattern) {
        this.bestPattern = bestPattern;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(SurveyResult o) {
        return this.score.compareTo(o.getScore());
    }
}

