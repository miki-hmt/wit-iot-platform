package com.witsoft.weilai.domain.iot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("device_timeline")
public class DeviceTimeline {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    //设备状态：0: 上电 1：运行 2：报警 3：关机
    private Integer status;

    //当前一条时序记录花费时长：秒
    private Long totalSpent;

    private String remark;
}
