package com.witsoft.gongli.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

    /**
     * @desc 求取两个时间段的差值，格式为秒
     * @param start
     * @param end
     * @return
     */
    public static Long getLeftSeconds(Date start, Date end){
        long time = start.getTime();
        long time1 = end.getTime();

        Long second = (time1 - time) / 1000;

        return second;
    }
}
