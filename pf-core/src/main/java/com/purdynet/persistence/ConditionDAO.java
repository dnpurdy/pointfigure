package com.purdynet.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.purdynet.condition.Condition;
import com.purdynet.scaling.impl.PercentScaling;

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
        if(condition.getTestScaling() instanceof PercentScaling)
        {
            obj.append("percentScaling", ((PercentScaling) condition.getTestScaling()).getPercent());
        }
        obj.append("percentReturn", condition.getPercentReturn());
        obj.append("daysHorizon", condition.getDaysHorizon());
        obj.append("minPatternSize", condition.getMinPatternSize());
        obj.append("maxPatternSize", condition.getMaxPatternSize());
        collection.insert(obj);
    }

    public static Condition getCondition(DB db, String collectionName)
    {
        DBCollection collection = db.getCollection(collectionName);
        DBObject obj = collection.findOne();

        if(obj.containsField("percentScaling"))
        {
            int percentReturn = (int) obj.get("percentReturn");
            int daysHorizon = (int) obj.get("daysHorizon");
            int minPatternSize = (int) obj.get("minPatternSize");
            int maxPatternSize = (int) obj.get("maxPatternSize");
            double percentScaling = (double) obj.get("percentScaling");
            return new Condition(new PercentScaling(percentScaling), percentReturn, daysHorizon, minPatternSize, maxPatternSize);
        }
        return null;
    }
}
