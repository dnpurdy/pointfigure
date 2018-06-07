package com.purdynet.data.impl

import java.io.File
import java.text.SimpleDateFormat
import java.util
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

import com.purdynet.data.{Downloader, SDownloader}
import com.purdynet.prices.PriceRecord
import org.apache.commons.lang3.time.DateUtils
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.annotation.tailrec
import scala.collection.JavaConverters._

@Component
@Profile(Array("dev"))
class RandomDownloader extends SDownloader with Downloader {
  val startPrice = 50
  val range = 0.02
  val fmt = new SimpleDateFormat("yyyy-MM-dd")

  override def getSPrices(symbol: String, start: Date, end: Date): Seq[PriceRecord] = {
    getPrices(symbol, start, end, Seq(), startPrice, range)
  }

  @tailrec
  private def getPrices(symbol: String, start: Date, end: Date, accu: Seq[PriceRecord], price: Double, range: Double): Seq[PriceRecord] = {
    if (start.after(end)) accu
    else getPrices(symbol, DateUtils.addDays(start, 1), end, accu :+ PriceRecord(start, BigDecimal(price)), price*(1 + ThreadLocalRandom.current.nextDouble(-1*range,1*range)), range)
  }

  override def download(symbol: String, start: Date, end: Date): File = throw new UnsupportedOperationException("Not working!")

  override def getPrices(symbol: String, start: Date, end: Date): util.List[PriceRecord] = getSPrices(symbol, start, end).map(sp => new PriceRecord(sp.getDateStr, sp.getJavaPrice)).asJava
}