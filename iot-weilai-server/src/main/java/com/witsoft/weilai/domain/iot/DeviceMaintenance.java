package com.witsoft.weilai.domain.iot;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("device_maintenance")
public class DeviceMaintenance {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;
    private String serialNumber;
    //质检工位
    private String qualityInspectionStation;
    //待检时长
    private String waitingTime;
    //质检人
    private String qualityInspector;
    //维修设备类型：0厂房 1车间 2流水线 3设备
    private String type;
    //厂房id
    private Integer factoryId;
    //车间id
    private Integer workshopId;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //0:未完成 1：完成
    private Boolean status;
}
