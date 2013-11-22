package com.purdynet

import com.purdynet.util.{DateUtil, FileUtil}
import com.purdynet.condition.Condition
import com.purdynet.scaling.impl.PercentScaling
import com.mongodb.{DB, MongoClient}
import com.purdynet.persistence.{PatternDAO, MongoUtils}
import com.purdynet.data.Downloader
import com.purdynet.data.impl.YahooDownloader
import java.util.{Collections, Date}
import com.purdynet.graph.PointFigureGraph
import com.purdynet.app.PresentScore
import java.lang.Double
import com.purdynet.pattern.PatternInstance
import akka.actor.{Props, ActorSystem}
import com.purdynet.actor.TodayScoreMaster
import akka.actor._
import akka.pattern.ask

import scala.collection.JavaConverters._

/**
 * Created with IntelliJ IDEA.
 * User: dpurdy
 * Date: 11/22/13
 * Time: 4:45 PM
 */
object TodaysScores extends App {
  val sh: TodaysScores = new TodaysScores
  sh.parseArgs(args)
  sh.run
}

class TodaysScores {
  var filename: String = _

  def parseArgs(args: Array[String]) {
    if (args.length == 0 || args.length > 1) System.exit(1)
    filename = args(0)
  }

  def run {
    val symbols: List[String] = FileUtil.getSymbolsFromFile(filename).asScala.toList
    val tCond: Condition = new Condition(new PercentScaling(0.01), 25, 120, 2, 15)
    val mongoClient: MongoClient = MongoUtils.getClient
    val db: DB = mongoClient.getDB("1382630345634")
    val size: Int = symbols.size
    var count: Int = 0

    val system = ActorSystem("TodayScoreSystem")

    // create the master
    val master = system.actorOf(Props(new TodayScoreMaster(8, db, tCond, symbols)), name = "master")

    // start the calculation
    val todaysScores = master ? "GO"
  }
}