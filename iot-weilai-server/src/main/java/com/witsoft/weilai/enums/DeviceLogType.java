package com.witsoft.weilai.enums;

public enum DeviceLogType {

    OPERATE_HISTORY(0, "操作记录"),
    DOT_CHECK(1, "点检记录"),
    MAINTAIN(2, "保养记录"),
    REPAIR_HISTORY(3, "维修记录"),
    TEMPERATURE_HISTORY(4, "温度异常统计"),
    MATERIAL_HISTORY(5, "物料异常时间统计");

    private Integer code;
    private String msg;

    DeviceLogType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
