package com.witsoft.gongli.model;


import lombok.Data;
import java.util.List;

/**
 * 设备列表指标实体
 */
@Data
public class DeviceQuotaListInfo {
    //所有设备平均合格率
    private String allQuantity = "0";
    //所有设备性能开动率
    private String allPerformance = "0";
    //所有设备时间开动率
    private String allAvailability = "0";
    private String allOee = "0";
    //所有设备稼动率
    private String allGrainMoveRate = "0";

    private List<DeviceQuota> list;
}
