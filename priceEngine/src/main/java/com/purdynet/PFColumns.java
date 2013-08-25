package com.purdynet;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/16/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PFColumns
{
    private Integer startBoxIdx;
    private Integer number;
    private XorO colType;

    public Integer getStartBoxIdx() {
        return startBoxIdx;
    }

    public Integer getLowBoxIdx()
    {
        switch(colType)
        {
            case X:
                return startBoxIdx;
            case O:
                return startBoxIdx-number+1;
        }
        return null;
    }

    public Integer getHighBoxIdx()
    {
        switch(colType)
        {
            case X:
                return startBoxIdx+number-1;
            case O:
                return startBoxIdx;
        }
        return null;
    }

    public enum XorO {
        X,
        O;
    }

    public PFColumns(XorO type, Integer startBoxIdx)
    {
        this.startBoxIdx = startBoxIdx;
        this.colType = type;
        this.number = 1;
    }

    public PFColumns(XorO type, Integer startBoxIdx, Integer number)
    {
        this.startBoxIdx = startBoxIdx;
        this.colType = type;
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public XorO getColType() {
        return colType;
    }
}
