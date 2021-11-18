package com.witsoft.gongli.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    public final static SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat sdf_s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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


    /**
     * @DESC 获取某天的零点，零分，零秒
     * @param date
     * @return
     */
    public static Date getCurrentZero(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * @desc 获取每个月的第一天的日期
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month){
        Calendar calendar = Calendar.getInstance();

        //设置月份
        calendar.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份最小的天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        String first = sdf_d.format(calendar.getTime()) + " 00:00:00";

        return first;
    }

    /**
     * @desc 获取每个月的第一天的时间戳
     * @param month
     * @return
     */
    public static Long getFirstDayTimeStampOfMonth(int month){
        Calendar calendar = Calendar.getInstance();

        //设置月份
        calendar.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份最小的天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        String first = sdf_d.format(calendar.getTime()) + " 00:00:00";

        try {
            Long time = sdf_d.parse(first).getTime();
            return time;
        } catch (ParseException e) {
            return 0l;
        }
    }


    /**
     * @desc 获取每个月的最后一天的日期
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int month){
        Calendar calendar = Calendar.getInstance();

        //设置月份
        calendar.set(Calendar.MONTH, month - 1);
        //获取某月最大的一天
        int lastDay = 0;
        //2月份的平年，瑞年天数
        if(month == 2){
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        }else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        //设置日历中月份最大的天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        String last = sdf_d.format(calendar.getTime()) + " 23:59:59";

        return last;
    }

    /**
     * @desc 获取每个月的最后一天的时间戳
     * @param month
     * @return
     */
    public static Long getLastDayTimeStampOfMonth(int month){
        Calendar calendar = Calendar.getInstance();

        //设置月份
        calendar.set(Calendar.MONTH, month - 1);
        //获取某月最大的一天
        int lastDay = 0;
        //2月份的平年，瑞年天数
        if(month == 2){
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        }else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        //设置日历中月份最大的天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        String last = sdf_d.format(calendar.getTime()) + " 23:59:59";

        try {
            return sdf_d.parse(last).getTime();
        } catch (ParseException e) {
            return 0l;
        }
    }
}
