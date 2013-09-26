package com.purdynet.graph;

import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.Scaling;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private Map<String,PFColumnPartialReference> columnDateMapTwo = new HashMap<String,PFColumnPartialReference>();
    private List<PFColumn> columns;

    public Map<String, PFColumnPartialReference> getColumnDateMapTwo() {
        return columnDateMapTwo;
    }

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
                columns.add(new PFColumn(currentPriceBoxIdx, currentPriceBoxIdx, ColumnType.X));
            }
            if(startPrice == null)
            {
                startPrice = currentPriceBoxIdx;
            }

            PFColumn lastCol = columns.get(columns.size()-1);

            switch(lastCol.getColType())
            {
                case X:
                    if(lastCol.getHighBoxIdx() - currentPriceBoxIdx >= 3)
                    {
                        columns.add(new PFColumn(currentPriceBoxIdx, lastCol.getHighBoxIdx()-1, ColumnType.O));
                    }
                    else
                    {
                        lastCol.setHighBoxIdx(Math.max(lastCol.getHighBoxIdx(),currentPriceBoxIdx));
                    }
                    break;
                case O:
                    if(currentPriceBoxIdx - lastCol.getLowBoxIdx() >= 3)
                    {
                        columns.add(new PFColumn(lastCol.getLowBoxIdx()+1, currentPriceBoxIdx, ColumnType.X));
                    }
                    else
                    {
                        lastCol.setLowBoxIdx(Math.min(lastCol.getLowBoxIdx(),currentPriceBoxIdx));
                    }
                    break;
            }

            priceDateMap.put(pr.getDateCode(),pr.getPrice());
            columnDateMap.put(pr.getDateCode(),new ArrayList<PFColumn>(columns));
            columnDateMapTwo.put(pr.getDateCode(), new PFColumnPartialReference(columns.size()-2, columns.get(columns.size()-1).toString()));
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

    public String getPattern(String datecode)
    {
        PFColumnPartialReference ref = columnDateMapTwo.get(datecode);
        if(ref.getStopColIdx()<0) return ref.getPostfixCol();
        else
        {
            StringBuilder pattern = new StringBuilder();
            for(int i=0; i<=ref.getStopColIdx(); i++)
            {
                pattern.append(columns.get(i).toString());
                pattern.append("-");
            }
            pattern.append(ref.getPostfixCol());
            return pattern.toString();
        }
    }

    public String getPattern(Integer length)
    {
        StringBuilder pattern = new StringBuilder();
        for(int i = columns.size()-1-length; i<=columns.size()-2; i++)
        {
            pattern.append(columns.get(i).toString());
            pattern.append("-");
        }
        pattern.append(columns.get(columns.size()-1));
        return pattern.toString();
    }

    public void render()
    {
        int blankCount = 0;

        int width = columns.size();
        int height = 0;
        for(PFColumn col : columns)
        {
            height = Math.max(height, col.getLowBoxIdx()+col.getNumber());
        }

        for(int i=height; i>=0; i--)
        {
            StringBuilder sb = new StringBuilder();
            DecimalFormat myFormatter = new DecimalFormat("00000.000");
            String output = myFormatter.format(getScaling().getValues().get(i));
            System.out.print(output+": ");
            for(int j=0; j<width; j++)
            {
                PFColumn curCol = columns.get(j);
                switch(columns.get(j).getColType())
                {
                    case X:
                        if(curCol.getLowBoxIdx()<=i && curCol.getHighBoxIdx()>=i) sb.append("X");
                        else sb.append(" ");
                        break;
                    case O:
                        if(curCol.getLowBoxIdx()<=i && curCol.getHighBoxIdx()>=i) sb.append("O");
                        else sb.append(" ");
                        break;
                }
            }
            System.out.println(sb.toString());
            if(sb.toString().replaceAll(" ","").length() == 0) blankCount++;
            if(blankCount==5) break;
        }
    }

}
