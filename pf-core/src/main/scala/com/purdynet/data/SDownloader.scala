package com.purdynet.data

import java.util.Date

import com.purdynet.prices.SPriceRecord

trait SDownloader {
  def getSPrices(symbol: String, start: Date, end: Date): Seq[SPriceRecord]
}
