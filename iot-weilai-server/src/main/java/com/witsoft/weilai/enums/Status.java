package com.witsoft.weilai.enums;

public enum Status {

    SUCCESS(0, "操作成功"),
    ERROR(999, "操作失败"),
    FAILED_TIMELINE(1000, "获取时序图失败"),
    REPORT_FAILED(1001, "设备状态上报失败"),
    DEVICE_INDEX_FAILED(1002, "设备指标查询异常");

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
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
