package com.witsoft.weilai.common.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.witsoft.weilai.domain.iot.StationRate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class StationRateDTO extends StationRate {

    /**
     * @desc 启动日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date useDate;
    //出厂编号
    private String factoryNumber;

    /**
     * device_name
     * 名称
     */
    private String deviceName;

    /**
     * device_model
     * 设备型号
     */
    private String deviceModel;

    /**
     * device_label
     * 标签
     */
    private String deviceLabel;

}
