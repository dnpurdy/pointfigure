package com.purdynet.data.impl

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

import com.purdynet.data.SDownloader
import com.purdynet.prices.SPriceRecord
import org.apache.commons.lang3.time.DateUtils
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
@Profile(Array("dev"))
class RandomDownloader extends SDownloader {
  val startPrice = 50
  val range = 0.02
  val fmt = new SimpleDateFormat("yyyy-MM-dd")

  override def getSPrices(symbol: String, start: Date, end: Date): Seq[SPriceRecord] = {
    getPrices(symbol, start, end, Seq(), startPrice, range)
  }

  @tailrec
  private def getPrices(symbol: String, start: Date, end: Date, accu: Seq[SPriceRecord], price: Double, range: Double): Seq[SPriceRecord] = {
    if (start.after(end)) accu
    else getPrices(symbol, DateUtils.addDays(start, 1), end, accu :+ SPriceRecord(start, BigDecimal(price)), price*(1 + ThreadLocalRandom.current.nextDouble(-1*range,1*range)), range)
  }
}