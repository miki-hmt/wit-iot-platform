package com.witsoft.weilai.domain.iot;

import com.baomidou.mybatisplus.annotation.TableName;
import com.witsoft.weilai.enums.DeviceType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @desc 设备统计表
 */
@Data
@TableName("device_rate")
public class DeviceRate {

    private Integer deviceRateId;
    private Integer deviceId;//设备id
    private String monthRunDist;//当月运行分布
    private String runtime;//运行总时间
    private String yearRunDist;//当年运行分布
    private String oee; //oee
    private String cuttingSpeed; //切割速度
    private String pressure;  //气压
    private String temperature; //设备温度
    private String voltage; //电压
    private String frequency;//频率
    private Date updateTime;//更新时间

    //平均故障时间min
    private String mtbf;
    //平均故障修复时间
    private String mttr;
    //生产节拍
    private String beat;
    //产线平衡率
    private String balanceRate;
    //故障率
    private String errorRate;

    //是否为数据库字段
    @TableField(exist = false)
    private DeviceType deviceType;


    //设备保养时间
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date equipmentMaintainDate;
}
