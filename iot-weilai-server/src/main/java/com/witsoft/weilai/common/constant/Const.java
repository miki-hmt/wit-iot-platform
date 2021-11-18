package com.witsoft.weilai.common.constant;

/**
 * 通用常量存储实体
 * @author miki
 */
public class Const {

    //设备状态
    public static final String RUNNING = "0";
    public static final String STOPPED = "1";
    public static final String WAITING = "2";
    public static final String FAILED = "3";
    //设备启动时间 早上7点半
    public static final Double MACHINE_START_TIME = 7.5;

    public static final String DEVICE_STATE_FIELD = "device_state";
    public static final String DEVICE_STATE_COUNT_FIELD = "stateCount";

    //加工节拍
    public static final Integer PROCESSING_BEAT = 2;
}
