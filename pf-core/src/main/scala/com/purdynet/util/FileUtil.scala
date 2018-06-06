package com.purdynet.util

import scala.collection.JavaConverters._
import scala.io.Source
/**
  * Created by dnpurdy on 11/2/16.
  */
object FileUtil {
  def getSymbolsFromFile(filename: String): List[String] = {
    Source.fromFile(filename).getLines.toList
  }

  def getSymbolsFromFileJava(filename: String): java.util.List[String] = {
    getSymbolsFromFile(filename).asJava
  }
}
