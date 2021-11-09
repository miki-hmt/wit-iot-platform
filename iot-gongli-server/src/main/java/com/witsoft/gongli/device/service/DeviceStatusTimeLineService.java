package com.witsoft.gongli.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.gongli.device.entity.DeviceStatusTimeLineEntity;
import java.util.List;

public interface DeviceStatusTimeLineService extends IService<DeviceStatusTimeLineEntity> {

    /**
     * @desc 不使用实时流处理
     * @param obj
     */
    void reportStatusNoSplitFlow(String obj);

    /**
     * @desc 使用实时流处理
     * @param obj
     */
    void reportStatusSplitFlow(String obj);

    /**
     * @desc 根据状态码获取当天的设备的最近一条记录
     * @param deviceId
     * @param status
     * @return
     */
    DeviceStatusTimeLineEntity getLastOneInfoByStatus(String deviceId, String status);

    /**
     * @desc 获取当天的时序图设备列表
     * @param deviceId
     * @return
     */
    List<DeviceStatusTimeLineEntity> getListByDeviceId(String deviceId);

    /**
     * @desc 取时间倒叙的第一条数据，以及倒数第一条数据
     * @param deviceId
     * @return
     */
    DeviceStatusTimeLineEntity getLastOneInfoByDeviceId(String deviceId);

    /**
     * @desc 取指定设备当日的运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    Long getSumRunningTimeDay(String deviceId);

    /**
     * @desc 取所有设备当日的运行时间总数（单位：秒）
     * @return
     */
    Long getSumAllRunningTimeDay();


    /**
     * @desc 取所有设备当日的开机时间总数（单位：秒）
     * @return
     */
    Long getSumAllOpeningTimeDay();

    /**
     * @desc 取上个月指定设备的的运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    Long getSumRunningTimeMonth(String deviceId);

    /**
     * @desc 取上个月指定设备的开机时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    Long getSumOpeningTimeMonth(String deviceId);

    /**
     * @desc 取设备当日的总时间（运行 + 待机 + 停机）（单位：秒）
     * @param deviceId
     * @return
     */
    Integer getSumTime(String deviceId);
}
