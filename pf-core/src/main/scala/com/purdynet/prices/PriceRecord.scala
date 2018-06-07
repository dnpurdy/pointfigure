package com.purdynet.prices

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

case class PriceRecord(date: Date, price: BigDecimal) extends Ordered[PriceRecord] {
  def this(dateString: String, price: BigDecimal) {
    this(PriceRecord.parseDateString(dateString), price)
  }

  @deprecated
  def getDateStr: String = PriceRecord.formatDateString(date)

  @deprecated
  def getJavaPrice: java.math.BigDecimal = new java.math.BigDecimal(price.longValue())

  def daysBetween(o: PriceRecord): Long = {
    val diff = o.date.getTime - this.date.getTime
    TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
  }

  override def toString: String = getDateStr + ": " + price.toString

  override def compare(that: PriceRecord): Int = this.date.compareTo(that.date)
}

object PriceRecord {
  final val sdf = new SimpleDateFormat("yyyy-MM-dd")

  def parseDateString(s: String): Date = sdf.parse(s)

  def formatDateString(d: Date): String = sdf.format(d)
}