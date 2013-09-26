package com.purdynet.persistence;

import com.mongodb.MongoClient;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/16/13
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MongoUtils
{
    private static final String host = "localhost";

    public static MongoClient getClient()
    {
        try {
            return new MongoClient( host );
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to connect to mongo!", e);
        }
    }
}
