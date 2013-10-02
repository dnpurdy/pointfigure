package com.purdynet.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.purdynet.condition.Condition;
import com.purdynet.pattern.PatternInstance;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 9/26/13
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConditionDAO
{
    public static void create(DB db, String collectionName, Condition condition)
    {
        DBCollection collection = db.getCollection(collectionName);
        BasicDBObject obj = new BasicDBObject();
        obj.append("scaling", condition.getTestScaling());
        obj.append("percentReturn", condition.getPercentReturn());
        obj.append("daysHorizon", condition.getDaysHorizon());
        obj.append("minPatternSize", condition.getMinPatternSize());
        obj.append("maxPatternSize", condition.getMaxPatternSize());
        collection.insert(obj);
    }
}
