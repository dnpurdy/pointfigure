package com.purdynet.scaling.impl

import java.math.{BigDecimal, MathContext}
import java.util
import java.util.Collections

/**
  * Created with IntelliJ IDEA.
  * User: dnpurdy
  * Date: 8/27/13
  * Time: 6:28 PM
  * To change this template use File | Settings | File Templates.
  */
@SerialVersionUID(201311232009010002L)
class PercentScaling(val initialValue: BigDecimal, val terminalValue: BigDecimal, var percent: Double) extends AbstractScaling {
  this.values = new util.ArrayList[BigDecimal]
  values.add(new BigDecimal(0))
  values.add(initialValue)
  while ( {
    values.get(values.size - 1).compareTo(terminalValue) < 0
  }) {
    val lastVal = values.get(values.size - 1)
    val nextVal = lastVal.multiply(new BigDecimal(1).add(new BigDecimal(percent)), MathContext.DECIMAL32)
    values.add(nextVal)
  }
  Collections.sort(values)

  def getPercent: Double = percent

  def this(percent: Double) {
    this(new BigDecimal(1), new BigDecimal(100000), percent)
  }
}