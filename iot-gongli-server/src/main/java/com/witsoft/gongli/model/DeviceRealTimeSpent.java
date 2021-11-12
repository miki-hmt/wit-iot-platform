package com.witsoft.gongli.model;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceRealTimeSpent {

    private String deviceId;

    private String status;

    private Long totalSpent;

    //与当前时间实时计算出的结果
    private Double realTimeSpent;

    private Date startTime;


    /**
     * @desc 自定义去重规则
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj){
        DeviceRealTimeSpent spent = (DeviceRealTimeSpent) obj;
        return deviceId.equals(spent.deviceId);
    }

    @Override
    public int hashCode(){
        return this.deviceId.hashCode();
    }
}
