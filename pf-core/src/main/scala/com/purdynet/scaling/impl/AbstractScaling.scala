package com.purdynet.scaling.impl

import java.io.Serializable
import java.math.BigDecimal
import java.util

import com.purdynet.scaling.Scaling

/**
  * Created with IntelliJ IDEA.
  * User: dnpurdy
  * Date: 8/27/13
  * Time: 6:26 PM
  * To change this template use File | Settings | File Templates.
  */
abstract class AbstractScaling extends Scaling with Serializable {
  protected var values: util.List[BigDecimal] = null

  override def getValues: util.List[BigDecimal] = values

  override def getIdx(searchValue: BigDecimal): Integer = getIdxByBTree(searchValue)

  def getIdxByLoop(searchValue: BigDecimal): Integer = {
    if (searchValue.compareTo(values.get(0)) < 0) return -1
    else {
      for(i <- 1 to values.size()) {
        if (values.get(i - 1).compareTo(searchValue) <= 0 && values.get(i).compareTo(searchValue) > 0) return i
      }
      values.size()
    }
  }

  def getIdxByBTree(price: BigDecimal): Integer = {
    var lowIdx = 0
    var highIdx = values.size
    var midIdx = (highIdx - lowIdx) / 2 + lowIdx
    while ( {
      lowIdx != midIdx && midIdx != highIdx
    }) {
      val lowVal = values.get(lowIdx)
      val midVal = values.get(midIdx)
      if (price.compareTo(lowVal) >= 0 && price.compareTo(midVal) < 0) {
        highIdx = midIdx
        midIdx = (highIdx - lowIdx) / 2 + lowIdx
      }
      else {
        lowIdx = midIdx
        midIdx = (highIdx - lowIdx) / 2 + lowIdx
      }
    }
    lowIdx
  }
}