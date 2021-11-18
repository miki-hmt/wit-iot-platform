package com.witsoft.weilai.enums;

/**
 * @desc 设备类型枚举
 * @author miki
 */
public enum DeviceType {
    DUI_HAN(1, "堆焊"),
    GEN_MACHINE_TOOL(2, "普通机床"),
    NC_MACHINE_TOOL(3, "数控机床"),
    HEAT_TREAT(4, "热处理"),
    STRESS_TESTING(5, "压力测试"),
    KEEP_WARM(6, "恒温恒湿仓库");

    private Integer code;
    private String msg;

    DeviceType(Integer code, String msg) {
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
