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

    public PointFigureGraph(List<PriceRecord> prices, Scaling s) {
        this(prices, s, 3);
    }

    public PointFigureGraph(List<PriceRecord> prices, Scaling s, Integer reversal)
    {
        this.scaling = s;

        try {
            Collections.sort(prices);
        } catch (Exception e) {
            //
        }

        Integer startPrice = null;
        for(PriceRecord pr : prices)
        {
            Integer currentPriceBoxIdx = scaling.getIdx(pr.getJavaPrice());
            if(columns == null)
            {
                columns = new ArrayList<PFColumn>();
                columns.add(new PFColumn(currentPriceBoxIdx, currentPriceBoxIdx, ColumnType.X()));
            }
            if(startPrice == null)
            {
                startPrice = currentPriceBoxIdx;
            }

            PFColumn lastCol = columns.get(columns.size()-1);

            if (lastCol.columnType().equals(ColumnType.X())) {
                if(lastCol.getHighBoxIdx() - currentPriceBoxIdx >= reversal)
                {
                    columns.add(new PFColumn(currentPriceBoxIdx, lastCol.getHighBoxIdx()-1, ColumnType.O()));
                }
                else
                {
                    lastCol.setHighBoxIdx(Math.max(lastCol.getHighBoxIdx(),currentPriceBoxIdx));
                }
            } else if (lastCol.columnType().equals(ColumnType.O())) {
                if(currentPriceBoxIdx - lastCol.getLowBoxIdx() >= reversal)
                {
                    columns.add(new PFColumn(lastCol.getLowBoxIdx()+1, currentPriceBoxIdx, ColumnType.X()));
                }
                else
                {
                    lastCol.setLowBoxIdx(Math.min(lastCol.getLowBoxIdx(),currentPriceBoxIdx));
                }
            }

            priceDateMap.put(pr.getDateStr(),pr.getJavaPrice());
            columnDateMap.put(pr.getDateStr(),deepCopy(columns));
            columnDateMapTwo.put(pr.getDateStr(), new PFColumnPartialReference(columns.size()-2, columns.get(columns.size()-1).toString()));
        }
    }

    private List<PFColumn> deepCopy(List<PFColumn> in) {
        List<PFColumn> out = new ArrayList<>(in.size());
        for (PFColumn col : in) {
            out.add(col.copy());
        }
        return out;
    }

    public List<BigDecimal> getScalingValues()
    {
        return scaling.getValues();
    }

    public List<PFColumn> getCurCols(final String dateString) {
        if (columnDateMap.containsKey(dateString)) return columnDateMap.get(dateString);
        else return new ArrayList<>();
    }

    public Set<String> getDates() {
        return columnDateMap.keySet();
    }

    public String getPattern(final String datecode)
    {
        PFColumnPartialReference ref = columnDateMapTwo.get(datecode);
        if(ref.stopColIdx()<0) return ref.postfixCol();
        else
        {
            StringBuilder pattern = new StringBuilder();
            for(int i=0; i<=ref.stopColIdx(); i++)
            {
                pattern.append(columns.get(i).toString());
                pattern.append("-");
            }
            pattern.append(ref.postfixCol());
            return pattern.toString();
        }
    }

    public String getPattern(String datecode, Integer length)
    {
        List<PFColumn> ref = columnDateMap.get(datecode);

            StringBuilder pattern = new StringBuilder();
            for(int i=length; i>0; i--)
            {
                if (i<=ref.size()) {
                    pattern.append(ref.get(ref.size()-i).getCode());
                } else {
                    pattern.append("0");
                }
                pattern.append(",");
            }
            return pattern.toString();

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
            String output = myFormatter.format(getScalingValues().get(i));
            System.out.print(output+": ");
            for(int j=0; j<width; j++)
            {
                PFColumn curCol = columns.get(j);

                if (columns.get(j).getColumnType().equals(ColumnType.X())) {
                    if(curCol.getLowBoxIdx()<=i && curCol.getHighBoxIdx()>=i) sb.append("X");
                    else sb.append(" ");
                } else if (columns.get(j).getColumnType().equals(ColumnType.O())) {
                    if(curCol.getLowBoxIdx()<=i && curCol.getHighBoxIdx()>=i) sb.append("O");
                    else sb.append(" ");
                }
            }
            System.out.println(sb.toString());
            if(sb.toString().replaceAll(" ","").length() == 0) blankCount++;
            if(blankCount==5) break;
        }
    }
}
