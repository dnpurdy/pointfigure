package com.purdynet.prices

import hirondelle.date4j.DateTime

/**
  * Created with IntelliJ IDEA.
  * User: dnpurdy
  * Date: 9/16/13
  * Time: 8:22 PM
  * To change this template use File | Settings | File Templates.
  */
case class ScoreRecord(date: DateTime, score: Double) extends Comparable[ScoreRecord] {
  def this(datecode: String, score: Double) = {
    this(new DateTime(datecode), score)
  }

  override def compareTo(o: ScoreRecord): Int = this.date.compareTo(o.date)

  override def toString: String = this.date.toString + "," + this.score
}