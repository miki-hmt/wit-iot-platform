package com.witsoft.weilai.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum MaintenanceType {
    FACTORY(0, "厂房"),
    WORKSHOP(1, "车间"),
    LINE(2, "流水线"),
    DEVICE(3, "设备");

    private Integer code;
    private String msg;

    MaintenanceType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static MaintenanceType getInstance(Integer code){
        Stream<MaintenanceType> typeStream = Arrays.stream(MaintenanceType.values()).filter(value -> value.getCode().equals(code));
        Optional<MaintenanceType> first = typeStream.findFirst();

        return first.orElse(null);
    }
}
