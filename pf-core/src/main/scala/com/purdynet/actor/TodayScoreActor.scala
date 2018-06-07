package com.purdynet.actor

import java.lang.Double
import java.util.Date

import akka.actor.{Actor, ActorLogging}
import com.mongodb.DB
import com.purdynet.app.PresentScore
import com.purdynet.condition.Condition
import com.purdynet.data.SDownloader
import com.purdynet.data.impl.YahooDownloader
import com.purdynet.graph.PointFigureGraph
import com.purdynet.message.{EmptyReply, TodayScoreWork}
import com.purdynet.pattern.PatternInstance
import com.purdynet.persistence.PatternDAO
import com.purdynet.util.DateUtil

import scala.collection.JavaConverters._

/**
 * Created with IntelliJ IDEA.
 * User: dpurdy
 * Date: 11/22/13
 * Time: 4:08 PM
 */
class TodayScoreActor extends Actor with ActorLogging {
  def receive: Actor.Receive = {
    case m: TodayScoreWork => RunTodayScore(m)
    case _ => throw new Exception("Unknown receive type")
  }

  def RunTodayScore(msg: TodayScoreWork) {
    log.info(msg.sym)
    val securitySym: String = msg.sym
    val tCond: Condition = msg.testCondition
    val db: DB = msg.db
    try {
      val d: SDownloader = new YahooDownloader
      val prices = d.getSPrices(securitySym, DateUtil.getDate(2016, 1, 1), new Date)
      val pfg: PointFigureGraph = new PointFigureGraph(prices.asJava, tCond.testScaling)
      var ps: PresentScore = null
      var i: Int = tCond.minPatternSize
      while (i <= tCond.maxPatternSize) {
        if (ps == null) {
          ps = new PresentScore(securitySym, PatternDAO.getScore(db, "patterns", pfg.getPattern(i)))
          ps.setPi(PatternDAO.getPatternInstance(db, "patterns", pfg.getPattern(i)))
        }
        else {
          val s: Double = PatternDAO.getScore(db, "patterns", pfg.getPattern(i))
          val pi: PatternInstance = PatternDAO.getPatternInstance(db, "patterns", pfg.getPattern(i))
          if (s > ps.getScore) {
            ps = new PresentScore(securitySym, s)
            ps.setPi(pi)
          }
        }
        i += 1
      }
      if(ps!=null) sender ! ps
      else sender ! new EmptyReply
    }
    catch {
      case e: Exception => {
        e.printStackTrace
        sender ! new EmptyReply
      }
    }
  }
}
