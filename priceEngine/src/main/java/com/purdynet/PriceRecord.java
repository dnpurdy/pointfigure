package com.purdynet;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/16/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceRecord implements Comparable<PriceRecord>
{
    private Integer year;
    private Integer month;
    private Integer day;
    private BigDecimal price;

    public PriceRecord(String dateString, BigDecimal price)
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateString));
            this.year = c.get(Calendar.YEAR);
            this.month = c.get(Calendar.MONTH)+1;
            this.day = c.get(Calendar.DAY_OF_MONTH);
            this.price = price;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return year.toString()+"-"+month.toString()+"-"+day.toString()+": "+price.toString();
    }

    @Override
    public int compareTo(PriceRecord o) {
        if(this.year.compareTo(o.getYear())!=0) return this.year.compareTo(o.getYear());
        else if(this.month.compareTo(o.getMonth())!=0) return this.month.compareTo(o.getMonth());
        else return this.day.compareTo(o.getDay());
    }
}