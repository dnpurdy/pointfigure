package com.purdynet.pattern;

import com.purdynet.condition.Condition;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/31/13
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatternContext implements Serializable
{
    static final long serialVersionUID = 201308230104001L;

    private Condition condition;
    private List<String> symbols;
    private Map<String, PatternInstance> patterns;
    private Map<String, Double> scores;

    public PatternContext()
    {}

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public Map<String, PatternInstance> getPatterns() {
        return patterns;
    }

    public void setPatterns(Map<String, PatternInstance> patterns) {
        this.patterns = patterns;
        this.scores = new HashMap<String,Double>();
        for(Map.Entry<String,PatternInstance> entry : patterns.entrySet())
        {
            scores.put(entry.getKey(), entry.getValue().getScore());
        }
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public static PatternContext readFromFile(String objectFile)
    {
        PatternContext readObject = null;
        try
        {
            //use buffering
            InputStream file = new FileInputStream( objectFile );
            InputStream buffer = new BufferedInputStream( file );
            ObjectInput input = new ObjectInputStream( buffer );

            try{
                readObject = (PatternContext) input.readObject();
            }
            finally{
                input.close();
            }
        }
        catch(IOException | ClassNotFoundException ex)
        {
            System.out.println("Cannot perform output.");
            ex.printStackTrace();
        }
        return readObject;
    }

    public static void writeToFile(PatternContext object, String objectFile)
    {
        try
        {
            //use buffering
            OutputStream file = new FileOutputStream( objectFile );
            OutputStream buffer = new BufferedOutputStream( file );
            ObjectOutput output = new ObjectOutputStream( buffer );
            try{
                output.writeObject(object);
            }
            finally{
                output.close();
            }
        }
        catch(IOException ex)
        {
            System.out.println("Cannot perform output.");
            ex.printStackTrace();
        }
    }

    public static String describeFile(String objectFile)
    {
        PatternContext pc = PatternContext.readFromFile(objectFile);
        return describe(pc);

    }

    public static String describe(PatternContext pc)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(pc.getCondition().toString());
        return sb.toString();
    }
}
