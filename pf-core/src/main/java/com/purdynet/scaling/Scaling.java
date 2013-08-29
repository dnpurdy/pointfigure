package com.purdynet.scaling;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/17/13
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Scaling
{
    Integer getIdx(BigDecimal price);

    List<BigDecimal> getValues();
}
