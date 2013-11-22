package com.purdynet.actor

import akka.actor.{Props, Actor, ActorRef}
import akka.routing.RoundRobinRouter
import com.purdynet.app.PresentScore
import com.purdynet.message.TodayScoreWork
import com.mongodb.DB
import com.purdynet.condition.Condition

/**
 * Created with IntelliJ IDEA.
 * User: dpurdy
 * Date: 11/22/13
 * Time: 4:29 PM
 */
class TodayScoreMaster(nrOfWorkers: Int, db: DB, tCond: Condition, syms: List[String]) extends Actor {

  var pi: Double = _
  var todaysScores: List[PresentScore] = List()
  var nrOfResults: Int = _
  val start: Long = System.currentTimeMillis

  val workerRouter = context.actorOf(
    Props[TodayScoreActor].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

  def receive = {
    case x: String => {
      for (i <- 1 to syms.size) workerRouter ! TodayScoreWork(db, syms(i), tCond)
    }
    case x: PresentScore => {
      nrOfResults += 1
      if(x!=null) x::todaysScores
      if (nrOfResults == syms.size) {
        sender ! todaysScores
      }
    }
  }
}
