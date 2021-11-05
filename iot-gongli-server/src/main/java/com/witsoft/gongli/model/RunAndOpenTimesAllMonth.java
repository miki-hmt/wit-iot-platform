package com.witsoft.gongli.model;

import lombok.Data;

/**
 * @desc 当月之前所有的月份运行时间，开机时间总和的实体
 */
@Data
public class RunAndOpenTimesAllMonth {

    //运行总时长
    private Long runningTimes;
    //开机总时长
    private Long openTimes;
    //时间
    private String date;
}
