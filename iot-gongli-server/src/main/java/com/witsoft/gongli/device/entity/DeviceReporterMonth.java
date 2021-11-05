package com.witsoft.gongli.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @desc 设备指标月度报表
 */
@Data
@TableName("device_month_reporter")
public class DeviceReporterMonth {
    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 时间稼动率
     */
    private String timeGrainMoveRate;
    /**
     * @desc 月度单设备总的运行时间
     */
    @TableField("month_running_time")
    private Long sunRunningTime;
    /**
     * @desc 月度单设备总的开机时间   --- 开机时间计算标准：当系统接收到关机事件时，时序库里寻找当日上一条最近的开机记录，计算当前时间和开机时间的差值，即是开机时间
     */
    @TableField("month_opening_time")
    private Long sumOpeningTime;

    private Date createTime;

    private String deviceId;

    public DeviceReporterMonth() {
    }

    public DeviceReporterMonth(Long sunRunningTime, Long sumOpeningTime, Date createTime, String deviceId) {
        this.sunRunningTime = sunRunningTime;
        this.sumOpeningTime = sumOpeningTime;
        this.createTime = createTime;
        this.deviceId = deviceId;
    }
}
