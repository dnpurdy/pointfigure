package com.purdynet.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.purdynet.pattern.PatternInstance;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/16/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatternDAO
{
    public static void create(DB db, String collectionName, PatternInstance pi)
    {
        DBCollection collection = db.getCollection(collectionName);
        BasicDBObject obj = new BasicDBObject();
        obj.append("pattern", pi.getPattern());
        obj.append("timesSeen", pi.getTimesSeen());
        obj.append("timesSuccessful", pi.getTimesSuccessful());
        obj.append("symSeen", pi.getSymbolsSeen());
        obj.append("symSuccessful", pi.getSymbolSuccessful());
        obj.append("score", pi.getScore());
        collection.insert(obj);
    }

    public static double getScore(DB db, String collectionName, String pattern)
    {
        DBCollection collection = db.getCollection(collectionName);
        DBObject obj = collection.findOne(new BasicDBObject("pattern", pattern));
        return obj != null ? (double) obj.get("score") : 0.0;
    }

    public static PatternInstance getPatternInstance(DB db, String collectionName, String pattern)
    {
        DBCollection collection = db.getCollection(collectionName);
        DBObject obj = collection.findOne(new BasicDBObject("pattern", pattern));

        if(obj!=null)
        {
            PatternInstance pi = new PatternInstance();
            pi.setPattern((String) obj.get("pattern"));
            pi.setTimesSeen((Integer) obj.get("timesSeen"));
            pi.setTimesSuccessful((Integer) obj.get("timesSuccessful"));
            return pi;
        }
        else
        {
            return null;
        }
    }
}
