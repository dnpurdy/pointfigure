package com.purdynet.message

import com.purdynet.condition.Condition
import com.mongodb.DB

/**
 * Created with IntelliJ IDEA.
 * User: dpurdy
 * Date: 11/22/13
 * Time: 4:19 PM
 */
case class TodayScoreWork(db: DB, sym: String, testCondition: Condition)
