package com.purdynet.condition

import com.purdynet.scaling.Scaling

/**
  * Created with IntelliJ IDEA.
  * User: dnpurdy
  * Date: 8/31/13
  * Time: 10:58 PM
  * To change this template use File | Settings | File Templates.
  */
@SerialVersionUID(201308232153002L)
case class Condition(var testScaling: Scaling, var percentReturn: Int, var daysHorizon: Int, var minPatternSize: Int, var maxPatternSize: Int) {}