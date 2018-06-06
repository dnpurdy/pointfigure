package com.purdynet.data

import java.io.File
import java.util
import java.util.Date

import com.purdynet.prices.PriceRecord

trait Downloader {
  def download(symbol: String, start: Date, end: Date): File
  def getPrices(symbol: String, start: Date, end: Date): util.List[PriceRecord]
}
