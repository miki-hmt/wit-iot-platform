package com.witsoft.weilai.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DeviceWarningDTO{

    private Integer warnId;
    //告警码
    private String warnCode;
    //告警信息
    private String warnMessage;
    //故障开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date warnStart;
    //故障完成率
    private String completeRate;

    //故障状态，0：未解决 1：已解决
    private Boolean status;

    private String deviceName;
}
