package com.witsoft.gongli.model;

import lombok.Data;

/**
 * 单设备指标实体
 */
@Data
public class DeviceQuota {
    //设备合格率
    private String quantity;
    //性能开动率
    private String performance;
    //时间开动率
    private String availability;
    //
    private String oee;
    //稼动率
    private String grainMoveRate;
    //
    private String deviceName;
}
