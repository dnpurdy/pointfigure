package com.purdynet.app

import java.util
import java.util.Date

import com.purdynet.data.SDownloader
import com.purdynet.data.impl.YahooDownloader
import com.purdynet.graph.PointFigureGraph
import com.purdynet.prices.PriceRecord
import com.purdynet.scaling.impl.TraditionalScaling
import com.purdynet.util.DateUtil

import scala.collection.JavaConverters._

/**
  * Created by dnpurdy on 11/2/16.
  */
object Tester extends App {
  val d: SDownloader = new YahooDownloader
  val prices = d.getSPrices("SPY", DateUtil.getDate(2001, 1, 1), new Date)
  val scaling = new TraditionalScaling
  val pfg: PointFigureGraph = new PointFigureGraph(prices.asJava, scaling)

  val daysForward = 90

  def CMM0(pricesRemain: Seq[PriceRecord], prices: Seq[PriceRecord], accu: Map[String,(BigDecimal,BigDecimal)]): Map[String,(BigDecimal,BigDecimal)] ={
    if (pricesRemain.isEmpty) accu
    else {
      val x = pricesRemain.head
      var min = x
      var max = x
      for (y: PriceRecord <- prices) {
        if (x.daysBetween(y) > 0 && x.daysBetween(y) < daysForward) {
          if (y.price.compareTo(max.price) > 0) max = y
          if (y.price.compareTo(min.price) < 0) min = y
        }
      }
      CMM0(pricesRemain.tail, prices, accu + (x.getDateStr -> ((((min.price - x.price) / (x.price)), (max.price - x.price) / (x.price)))))
    }
  }

  def cMM(prices: Seq[PriceRecord]): Map[String,(BigDecimal,BigDecimal)] = {
    CMM0(prices, prices, Map())
  }


  def computeMinMix(prices: util.List[PriceRecord]) = {
    for (x: PriceRecord <- prices.asScala) {
      var min = x
      var max = x
      for (y: PriceRecord <- prices.asScala) {
        if (x.daysBetween(y) > 0 && x.daysBetween(y) < daysForward) {
          if (y.price.compareTo(max.price) > 0) max = y
          if (y.price.compareTo(min.price) < 0) min = y
        }
      }
      System.out.println((x, ((((min.price - x.price) / (x.price)), (max.price - x.price) / (x.price)))))
    }
  }

  val pS: Seq[PriceRecord] = prices
  val minmax = cMM(pS)

  for(pr <- pS) {
    val pattern: String = pfg.getPattern(pr.getDateStr,4)
    val min = minmax.get(pr.getDateStr).get._1
    val max = minmax.get(pr.getDateStr).get._2
    val minus = if(min.compare(BigDecimal(-0.20)) < 0) "1" else "0"
    val minus20 = if(min.compare(BigDecimal(-0.20)) > 0 && min.compare(BigDecimal(-0.10)) < 0) "1" else "0"
    val minus10 = if(min.compare(BigDecimal(-0.10)) > 0 && min.compare(BigDecimal(-0.05)) < 0) "1" else "0"
    val minus5 = if(min.compare(BigDecimal(-0.05)) > 0 && min.compare(BigDecimal(0)) < 0) "1" else "0"
    val plus5 = if(max.compare(BigDecimal(0)) > 0 && max.compare(BigDecimal(0.05)) < 0) "1" else "0"
    val plus10 = if(max.compare(BigDecimal(0.05)) > 0 && max.compare(BigDecimal(0.10)) < 0) "1" else "0"
    val plus20 = if(max.compare(BigDecimal(0.10)) > 0  && max.compare(BigDecimal(0.20)) < 0) "1" else "0"
    val plus = if(max.compare(BigDecimal(0.20)) > 0) "1" else "0"
    System.out.println(pattern+min+","+max)
  }
}


