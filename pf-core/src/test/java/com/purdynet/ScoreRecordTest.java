package com.purdynet;

import com.purdynet.prices.ScoreRecord;
import hirondelle.date4j.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/22/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreRecordTest
{
    @Test
    public void scoreRecordTest_1()
    {
        ScoreRecord sr = new ScoreRecord("2013-01-01", 35.6);
        assertEquals(sr.date(), new DateTime("2013-01-01"));
        assertEquals(new Double(sr.score()), new Double(35.6));
    }

    @Test
    public void scoreRecordTest_2()
    {
        ScoreRecord sr1 = new ScoreRecord("2013-01-01", 35.6);
        ScoreRecord sr2 = new ScoreRecord("2013-01-02", 35.9);
        assertTrue(sr1.compareTo(sr2) < 0);
    }
}
