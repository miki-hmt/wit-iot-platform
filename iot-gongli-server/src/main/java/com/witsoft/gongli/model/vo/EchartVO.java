package com.witsoft.gongli.model.vo;

import lombok.Data;

@Data
public class EchartVO {

    private String name;
    private Integer value;
    private String status;

    public EchartVO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
