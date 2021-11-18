package com.witsoft.weilai.domain.iot;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("station_rate")
public class StationRate {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer stationId;
    //订单完成率
    private String orderCompletionRate;
    //节拍数
    private String beat;
    //环境温度
    private String envTemperature;
    //环境湿度
    private String envWet;
    //voc
    private String voc;
    //订单编号
    private String orderCode;
    //产品型号
    private String productModel;
    //序列号
    private String serialNumber;
    //生产时间
    private String productTime;
    //oee
    private String oee;
    //当前电压
    private String currentVoltage;
    //当前电流
    private String currentElectric;
    //当前电量
    private String currentElectricQuantity;
}
