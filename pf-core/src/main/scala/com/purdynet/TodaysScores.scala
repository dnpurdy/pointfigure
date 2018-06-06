package com.purdynet

import akka.actor._
import akka.util.Timeout
import com.mongodb.{DB, MongoClient}
import com.purdynet.actor.{TodayScoreListener, TodayScoreMaster}
import com.purdynet.condition.Condition
import com.purdynet.persistence.MongoUtils
import com.purdynet.scaling.impl.PercentScaling
import com.purdynet.util.FileUtil

import scala.concurrent.duration._

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
    val symbols: List[String] = FileUtil.getSymbolsFromFile(filename)
    //val symbols: List[String] = List("GE","VZ","WAG")
    val tCond: Condition = new Condition(new PercentScaling(0.01), 25, 120, 2, 15)
    val mongoClient: MongoClient = MongoUtils.getClient
    val db: DB = mongoClient.getDB("1528000332712")
    val size: Int = symbols.size
    var count: Int = 0

    val system = ActorSystem("TodayScoreSystem")
    val listener = system.actorOf(Props[TodayScoreListener], name = "listener")
    // create the master
    val master = system.actorOf(Props(new TodayScoreMaster(16, db, tCond, symbols, listener)), name = "master")

    implicit val timeout = Timeout(10 minutes)

    // start the calculation
    val todaysScores = master ! "GO"
  }
}