package com.witsoft.gongli.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("device_status_timeline")
public class DeviceStatusTimeLineEntity {

    private String id;
    private String deviceId;
    /**
     * 状态码：0：开机 1：正常 2：空闲 3：故障 4：关机
     */
    private String status;
    /**
     * 事件开始时间
     * 设备时序图的起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    //设备时序图的结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 接收的事件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventTime;
    /**
     * 开机时间时长，运行时间时长 格式：S
     */
    private Long totalSpent;
}
