package com.purdynet.graph;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/28/13
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PFColumn
{
    private Integer startBoxIdx;
    private Integer number;
    private ColumnType colType;

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
                return startBoxIdx+number;
            case O:
                return startBoxIdx;
        }
        return null;
    }

    public PFColumn(Integer startBoxIdx, ColumnType colType)
    {
        this.startBoxIdx = startBoxIdx;
        this.colType = colType;
        this.number = 1;
    }

    public PFColumn(Integer startBoxIdx, ColumnType colType, Integer number)
    {
        this.startBoxIdx = startBoxIdx;
        this.colType = colType;
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ColumnType getColType() {
        return colType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PFColumn pfColumn = (PFColumn) o;

        if (colType != pfColumn.colType) return false;
        if (!number.equals(pfColumn.number)) return false;
        if (!startBoxIdx.equals(pfColumn.startBoxIdx)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startBoxIdx.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + colType.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return getNumber().toString()+getColType().name().toString();
    }
}
