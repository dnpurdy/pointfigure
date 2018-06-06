package com.purdynet.util

import java.util.{Calendar, Date}

/**
  * Created by dnpurdy on 11/2/16.
  */
object DateUtil {
  def getDate(year: Int, month: Int, day: Int): Date = {
    val c: Calendar = Calendar.getInstance
    c.set(year, month - 1, day)
    c.getTime
  }
}
