package com.purdynet.actor

import akka.actor.Actor
import com.purdynet.app.PresentScore

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 11/23/13
 * Time: 9:39 AM
 * To change this template use File | Settings | File Templates.
 */
class TodayScoreListener extends Actor {
  def receive = {
    case x: List[PresentScore] => {
      for(i: PresentScore <- x )
      {
        println(i)
      }
      context.system.terminate()
    }
  }
}
