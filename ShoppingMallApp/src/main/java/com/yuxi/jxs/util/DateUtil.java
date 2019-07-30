package com.yuxi.jxs.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author 陈开.
 * Date: 2019/6/25
 * Time: 13:59
 */
public class DateUtil {
    public static String dateToStrLong(Date dateDate, String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        String dateString = formatter.format(dateDate);
        return dateString;
    }
}
