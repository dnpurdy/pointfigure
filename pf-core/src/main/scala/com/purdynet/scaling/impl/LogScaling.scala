package com.purdynet.scaling.impl

import java.math.BigDecimal
import java.util

/**
  * Created with IntelliJ IDEA.
  * User: dnpurdy
  * Date: 8/27/13
  * Time: 7:17 PM
  * To change this template use File | Settings | File Templates.
  */
@SerialVersionUID(201311232009010001L)
class LogScaling(var factor: Double) extends AbstractScaling {
  this.values = new util.ArrayList[BigDecimal]
  var i = new BigDecimal("1")
  while ( {
    i.compareTo(new BigDecimal(100000)) < 0
  }) {
    i = new BigDecimal(Math.pow(10, Math.log10(i.doubleValue) + factor))
    values.add(i)
  }
}

object LogScaling {
  val ONE_FIVE = new LogScaling(1.5)
}