package com.purdynet.data.impl

import java.io.File
import java.util
import java.util.Date

import com.purdynet.data.{Downloader, SDownloader}
import com.purdynet.prices.{PriceRecord, SPriceRecord}

class YahooDownloader extends Downloader with SDownloader {
  override def download(symbol: String, start: Date, end: Date) = new File("/tmp/yahoo.csv")

  override def getPrices(symbol: String, start: Date, end: Date): util.List[PriceRecord] = new util.ArrayList[PriceRecord]()

  override def getSPrices(symbol: String, start: Date, end: Date): Seq[SPriceRecord] = Seq()
}