package com.purdynet.data;

import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/17/13
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Downloader {
    File download(String symbol, Date start, Date end);
}
