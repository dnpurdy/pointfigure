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
    private Integer lowBoxIdx;
    private Integer highBoxIdx;
    private ColumnType colType;

    public Integer getLowBoxIdx()
    {
        return lowBoxIdx;
    }

    public void setLowBoxIdx(Integer lowBoxIdx)
    {
        this.lowBoxIdx = lowBoxIdx;
    }

    public Integer getHighBoxIdx()
    {
        return highBoxIdx;
    }

    public void setHighBoxIdx(Integer highBoxIdx)
    {
        this.highBoxIdx = highBoxIdx;
    }

    public PFColumn(Integer lowBoxIdx, Integer highBoxIdx, ColumnType colType)
    {
        this.lowBoxIdx = lowBoxIdx;
        this.highBoxIdx = highBoxIdx;
        this.colType = colType;
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
        if (highBoxIdx != null ? !highBoxIdx.equals(pfColumn.highBoxIdx) : pfColumn.highBoxIdx != null) return false;
        if (lowBoxIdx != null ? !lowBoxIdx.equals(pfColumn.lowBoxIdx) : pfColumn.lowBoxIdx != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lowBoxIdx != null ? lowBoxIdx.hashCode() : 0;
        result = 31 * result + (highBoxIdx != null ? highBoxIdx.hashCode() : 0);
        result = 31 * result + (colType != null ? colType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return getNumber()+getColType().name().toString();
    }

    public Integer getNumber()
    {
        return highBoxIdx-lowBoxIdx+1;
    }
}
