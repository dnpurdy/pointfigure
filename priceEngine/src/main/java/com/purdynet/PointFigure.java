package com.purdynet;

import com.purdynet.prices.PriceRecord;
import com.purdynet.scaling.Scaling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/16/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointFigure {

    private Scaling s;
    private Integer startPrice;
    private List<PFColumns> columns;

    public int getNumberOfColumns()
    {
        return columns.size();
    }

    public PointFigure(List<PriceRecord> prices, Scaling s)
    {
        this.s = s;

        Collections.sort(prices);

        for(PriceRecord pr : prices)
        {
            Integer currentPriceBoxIdx = s.getIdx(pr.getPrice());
            if(columns == null)
            {
                columns = new ArrayList<PFColumns>();
                columns.add(new PFColumns(PFColumns.XorO.X, currentPriceBoxIdx));
            }
            if(startPrice == null)
            {
                startPrice = currentPriceBoxIdx;
            }

            PFColumns lastCol = columns.get(columns.size()-1);

            switch(lastCol.getColType())
            {
                case X:
                    if(lastCol.getHighBoxIdx() - currentPriceBoxIdx >= 3)
                    {
                        columns.add(new PFColumns(PFColumns.XorO.O,lastCol.getHighBoxIdx()-1,lastCol.getHighBoxIdx() - currentPriceBoxIdx));
                    }
                    else
                    {
                        lastCol.setNumber(Math.max(lastCol.getNumber(), currentPriceBoxIdx-lastCol.getStartBoxIdx()));
                    }
                    break;
                case O:
                    if(currentPriceBoxIdx - lastCol.getLowBoxIdx() >= 3)
                    {
                        columns.add(new PFColumns(PFColumns.XorO.X,lastCol.getLowBoxIdx()+1,currentPriceBoxIdx - lastCol.getLowBoxIdx()));
                    }
                    else
                    {
                        lastCol.setNumber(Math.max(lastCol.getNumber(), lastCol.getStartBoxIdx()-currentPriceBoxIdx));
                    }
                    break;
            }
        }
    }

    public void render()
    {
        int blankCount = 0;

        System.out.println(getPatternCode(3));

        int width = columns.size();
        int height = 0;
        for(PFColumns col : columns)
        {
            switch (col.getColType())
            {
                case X:
                    height = Math.max(height, col.getStartBoxIdx()+col.getNumber());
                    break;
                case O:
                    height = Math.max(height, Math.abs(col.getStartBoxIdx()-col.getNumber()));
            }
        }

        for(int i=height; i>=0; i--)
        {
            StringBuilder sb = new StringBuilder();
            DecimalFormat myFormatter = new DecimalFormat("00000.000");
            String output = myFormatter.format(s.getValues().get(i));
            System.out.print(output+": ");
            for(int j=0; j<width; j++)
            {
                PFColumns curCol = columns.get(j);
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

    public String getPatternCode(int numCols)
    {
        StringBuilder code = new StringBuilder();
        for(int i=columns.size()-numCols; i<columns.size(); i++)
        {
            code.append(columns.get(i).getNumber().toString()).append(columns.get(i).getColType()).append("-");
        }
        return code.toString();
    }

}
