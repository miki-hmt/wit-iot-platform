package com.witsoft.weilai.utils;

import com.witsoft.common.constant.Const;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class TimeUtil {


    public static String getCurrentHour(){
        Integer hour = LocalDateTime.now().getHour();
        Integer minute = LocalDateTime.now().getMinute();
        BigDecimal b1 = new BigDecimal(minute);
        //60分钟
        BigDecimal b2 = new BigDecimal(60);

        BigDecimal left = b1.divide(b2, 1, RoundingMode.HALF_UP);
        Double total = hour.doubleValue() + left.doubleValue();

        //机器从早上7点半开始运行
        if(total < Const.MACHINE_START_TIME){
            total = 0.0;
        }else{
            total = new BigDecimal(total).subtract(new BigDecimal(Const.MACHINE_START_TIME)).abs().setScale(1,1).doubleValue();
        }

        //设备每日运行最大上限是10小时
        if(total > 10.0){
            total = 10.00;
        }
        return String.valueOf(total);
    }


    public static void main(String[] args) {
        System.out.println(getCurrentHour());

        System.out.println((double) 20/60);
    }
}
