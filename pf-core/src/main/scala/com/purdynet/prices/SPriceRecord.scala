package com.purdynet.prices

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

case class SPriceRecord(date: Date, price: BigDecimal) extends Ordered[SPriceRecord] {
  def this(dateString: String, price: BigDecimal) {
    this(SPriceRecord.parseDateString(dateString), price)
  }

  @deprecated
  def getDateStr: String = SPriceRecord.formatDateString(date)

  @deprecated
  def getJavaPrice: java.math.BigDecimal = new java.math.BigDecimal(price.longValue())

  def daysBetween(o: SPriceRecord): Long = {
    val diff = o.date.getTime - this.date.getTime
    TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
  }

  override def toString: String = getDateStr + ": " + price.toString

  override def compare(that: SPriceRecord): Int = this.date.compareTo(that.date)
}

object SPriceRecord {
  final val sdf = new SimpleDateFormat("yyyy-MM-dd")

  def parseDateString(s: String): Date = sdf.parse(s)

  def formatDateString(d: Date): String = sdf.format(d)
}