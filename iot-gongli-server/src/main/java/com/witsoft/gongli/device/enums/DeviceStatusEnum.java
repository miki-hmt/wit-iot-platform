package com.witsoft.gongli.device.enums;

public enum DeviceStatusEnum {
    MACHINE_OFFLINE(0, "关机"),
    MACHINE_ONLINE(1, "开机");

    private Integer code;
    private String msg;

    DeviceStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
