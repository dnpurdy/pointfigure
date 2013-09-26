package com.purdynet.prices;

import hirondelle.date4j.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/16/13
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreRecord implements Comparable<ScoreRecord>
{
    private DateTime date;
    private Double score;

    public ScoreRecord(String datecode, double score)
    {
        this.date = new DateTime(datecode);
        this.score = score;
    }

    public DateTime getDate() {
        return date;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoreRecord o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public String toString()
    {
        return this.getDate().toString()+","+this.getScore();
    }
}
