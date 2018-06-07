package com.purdynet.graph;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/28/13
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PFColumnTest
{
    @Test
    public void testPFColumnEquals()
    {
        PFColumn pc1 = new PFColumn(1,1,ColumnType.X());
        PFColumn pc2 = new PFColumn(1,1,ColumnType.X());
        PFColumn pc3 = new PFColumn(2,3,ColumnType.O());

        assertEquals(pc1,pc2);
        assertNotEquals(pc2,pc3);
    }
}
