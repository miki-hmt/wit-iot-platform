package com.witsoft.gongli.model.bo;

import lombok.Data;

/**
 * @desc 单个设备指标的原始数据实体模型
 */
@Data
public class DeviceQuotaBO {
    //设备合格率
    private Double quantity;
    //性能开动率
    private Double performance;
    //时间开动率
    private Double availability;

    private Double oee;

    //稼动率
    private Double grainMoveRate;

    private String deviceName;

    private String serialNumber;
}
