package com.witsoft.weilai.enums;

public enum DeviceStateEnum {

    RUNNING(0, "上电"),
    STOPPED(1, "运行"),
    WAITING(2, "报警"),
    FAILED(3, "关机");

    private Integer code;
    private String msg;

    DeviceStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
