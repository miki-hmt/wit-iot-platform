package com.witsoft.gongli.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.witsoft.gongli.model.vo.EchartVO;
import lombok.Data;

import java.util.List;

@Data
public class DeviceTotalInfo {

    private Integer status;
    private Integer count;

    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private List<EchartVO> data;
    private Integer runningNum;
    private Integer errorNum;

    private String grainMoveRate;

    public DeviceTotalInfo(Integer status, Integer total) {
        this.status = status;
        this.total = total;
    }

    public DeviceTotalInfo(Integer total, List<EchartVO> data) {
        this.total = total;
        this.data = data;
    }
}
