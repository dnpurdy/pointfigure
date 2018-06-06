package com.purdynet.util

import com.purdynet.graph.PFColumn

import scala.collection.JavaConverters._
/**
  * Created by dnpurdy on 11/2/16.
  */
object PFColumnUtil {
  def getPattern(columnsJ: java.util.List[PFColumn], patternSize: Integer): String = {
    val columns = columnsJ.asScala
    if (patternSize > columns.size) null
    else {
      val sb: StringBuilder = new StringBuilder
      var i: Int = columns.size - 1 - patternSize
      while (i < columns.size) {
        {
          sb.append(columns(i).toString)
          if (i != columns.size - 1) sb.append("-")
        }
        {
          i += 1; i - 1
        }
      }
      sb.toString
    }
  }
}
