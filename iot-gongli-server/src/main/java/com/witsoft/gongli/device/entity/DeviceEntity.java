package com.witsoft.gongli.device.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("device")
public class DeviceEntity {
    @TableId("id")
    private String id;

    /**
     * @desc 设备类型：设备（default），厂级（factory_line）,车间级（workshop_line）
     */
    @TableField("type")
    private String type;

    @TableField("alias")
    private String alias;

    @TableField("name")
    private String name;

    @TableField("model_number")
    private String modelNumber;

    @TableField("serial_number")
    private String serialNumber;

    @TableField("location")
    private String location;

    @TableField("kpi_cala_period")
    private String kpiCalaPeriod;

    @TableField("device_meter")
    private Double deviceMeter;

    /**
     * @desc 计划开始时间
     */
    @TableField("plan_start_time")
    private Long planStartTime;

    @TableField("plan_end_time")
    private Long planEndTime;
    /**
     * @desc 良好的产品数量
     */
    @TableField("good_count")
    private Long goodCount;

    /**
     * @desc 残缺的产品数量
     */
    @TableField("bad_count")
    private Long badCount;

    /**
     * @desc 产品总数量
     */
    @TableField("total_count")
    private Long totalCount;

    @TableField("ideal_run_rate")
    private Long idealRunRate;

    @TableField("sort")
    private Integer sort;

    @TableField("status")
    //0:故障 1：正常 2：空闲 3：开机 4：停机
    private String status;

    @TableField("created_time")
    @JsonProperty("createTime")
    private Long createTime;
}
