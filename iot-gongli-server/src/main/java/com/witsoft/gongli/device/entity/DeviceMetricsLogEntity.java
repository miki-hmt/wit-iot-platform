package com.witsoft.gongli.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("device_metrics_log")
public class DeviceMetricsLogEntity {

    private String id;

    private String deviceId;

    private String availability;

    private String performance;

    private String quality;

    private String oee;

    private Date createTime;
    private Long goodCount;

    private Long badCount;

    private Long totalCount;
    /**
     * 设备类型：设备（default），厂级（factory_line）,车间级（workshop_line）
     */
    private String type;
}
