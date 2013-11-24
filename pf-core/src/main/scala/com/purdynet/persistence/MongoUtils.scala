package com.purdynet.persistence

import com.mongodb.MongoClient

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 11/17/13
 * Time: 8:42 PM
 */
object MongoUtils {
  def getClient: MongoClient = {
    getClient("localhost")
  }

  def getClient(host:String): MongoClient = {
    try
      new MongoClient(host)

    catch {
      case e: Exception => {
        throw new RuntimeException("Failed to connect to mongo!", e)
      }
    }
  }
}
