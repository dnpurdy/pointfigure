package com.purdynet.actor

import akka.actor.{ActorLogging, Actor}
import com.purdynet.data.Downloader
import com.purdynet.data.impl.YahooDownloader
import com.purdynet.util.DateUtil
import java.util.Date
import com.purdynet.graph.PointFigureGraph
import com.purdynet.app.PresentScore
import com.purdynet.persistence.PatternDAO
import java.lang.Double
import com.purdynet.pattern.PatternInstance
import com.purdynet.message.{EmptyReply, TodayScoreWork}
import com.purdynet.condition.Condition

import com.mongodb.DB

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
      val d: Downloader = new YahooDownloader
      val prices = d.getPrices(securitySym, DateUtil.getDate(2001, 1, 1), new Date)
      val pfg: PointFigureGraph = new PointFigureGraph(prices, tCond.getTestScaling)
      var ps: PresentScore = null
      var i: Int = tCond.getMinPatternSize
      while (i <= tCond.getMaxPatternSize) {
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
