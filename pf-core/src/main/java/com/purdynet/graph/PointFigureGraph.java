package com.purdynet.graph;

import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.Scaling;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/28/13
 * Time: 6:53 PM
 */
public class PointFigureGraph
{
    private Scaling scaling;
    private Map<String,BigDecimal> priceDateMap = new HashMap<String,BigDecimal>();
    private Map<String,List<PFColumn>> columnDateMap = new HashMap<String,List<PFColumn>>();
    private List<PFColumn> columns;

    public PointFigureGraph(List<PriceRecord> prices, Scaling s)
    {
        this.scaling = s;

        Collections.sort(prices);

        Integer startPrice = null;
        for(PriceRecord pr : prices)
        {
            Integer currentPriceBoxIdx = scaling.getIdx(pr.getPrice());
            if(columns == null)
            {
                columns = new ArrayList<PFColumn>();
                columns.add(new PFColumn(currentPriceBoxIdx, ColumnType.X));
            }
            if(startPrice == null)
            {
                startPrice = currentPriceBoxIdx;
            }

            PFColumn lastCol = columns.get(columns.size()-1);

            switch(lastCol.getColType())
            {
                case X:
                    if(lastCol.getHighBoxIdx() - currentPriceBoxIdx > 3)
                    {
                        columns.add(new PFColumn(lastCol.getHighBoxIdx()-1,ColumnType.O, lastCol.getHighBoxIdx() - currentPriceBoxIdx));
                    }
                    else
                    {
                        lastCol.setNumber(Math.max(lastCol.getNumber(), currentPriceBoxIdx-lastCol.getStartBoxIdx()));
                    }
                    break;
                case O:
                    if(currentPriceBoxIdx - lastCol.getLowBoxIdx() > 3)
                    {
                        columns.add(new PFColumn(lastCol.getLowBoxIdx()+1,ColumnType.X, currentPriceBoxIdx - lastCol.getLowBoxIdx()));
                    }
                    else
                    {
                        lastCol.setNumber(Math.max(lastCol.getNumber(), lastCol.getStartBoxIdx()-currentPriceBoxIdx));
                    }
                    break;
            }

            priceDateMap.put(pr.getDateCode(),pr.getPrice());
            columnDateMap.put(pr.getDateCode(),new ArrayList<PFColumn>(columns));
        }
    }

    public Scaling getScaling()
    {
        return scaling;
    }

    public Map<String, BigDecimal> getPriceDateMap()
    {
        return priceDateMap;
    }

    public Map<String, List<PFColumn>> getColumnDateMap()
    {
        return columnDateMap;
    }

    public List<PFColumn> getColumns()
    {
        return columns;
    }
}
