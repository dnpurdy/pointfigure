package com.purdynet.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/2/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil
{
    public static Date getDate(int year, int month, int day)
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day);
        return c.getTime();
    }

    public static Integer getYear(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR) - 1900;
    }
}
