package com.purdynet.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/16/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SymbolDAO
{
    public static void create(DB db, String collectionName, String symbol)
    {
        DBCollection collection = db.getCollection(collectionName);
        collection.insert(new BasicDBObject("ticker", symbol));
    }

}
