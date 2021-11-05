package com.witsoft.gongli.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @desc 所有月份的运行时间
 */
@Data
public class RunningTimesAllMonth {

    @TableField("sum")
    private Long total;
    private String date;
}
