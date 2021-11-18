package com.witsoft.gongli.device.enums;

public enum MachineStatusEnum {

    ERROR(0, "故障", "0", ""),
    RUNNING(1, "正常", "1", ""),
    WAITING(2, "待机", "0", ""),
    TURNING(3, "开机", "1", ""),
    STOPPING(4, "停机", "0", "关机");

    private Integer code;
    private String msg;
    private String value;
    private String alias;

    MachineStatusEnum(Integer code, String msg, String value, String alias) {
        this.code = code;
        this.msg = msg;
        this.value = value;
        this.alias = alias;
    }

    public static MachineStatusEnum getInstance(Integer code){
        MachineStatusEnum[] values = MachineStatusEnum.values();
        for (MachineStatusEnum statusEnum: values) {
            if(statusEnum.code.equals(code)){
                return statusEnum;
            }
        }

        return ERROR;
    }

    public Integer getCode() {
        return code;
    }

    public String getCodeStr(){
        return code.toString();
    }

    public String getMsg() {
        return msg;
    }

    public String getValue() {
        return value;
    }

    public String getAlias() {
        return alias;
    }
}
