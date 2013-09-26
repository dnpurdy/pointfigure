package com.purdynet.graph;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/30/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PFColumnPartialReference
{
    private final Integer stopColIdx;
    private final String postfixCol;

    public PFColumnPartialReference(Integer stopColIdx, String postfixCol)
    {
        this.stopColIdx = stopColIdx;
        this.postfixCol = postfixCol;
    }

    public Integer getStopColIdx() {
        return stopColIdx;
    }

    public String getPostfixCol() {
        return postfixCol;
    }
}
