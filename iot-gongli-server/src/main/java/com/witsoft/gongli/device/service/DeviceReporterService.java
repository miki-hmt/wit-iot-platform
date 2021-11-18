package com.witsoft.gongli.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;

import java.util.Map;

public interface DeviceReporterService extends IService<DeviceReporterMonth> {

    /**
     * @desc 获取所有设备当月的时间稼动率
     */
    Map<String, Object> getAllDeviceMonthGrainMoveRate();


    /**
     * @desc 获取所有设备当月的性能稼动率
     */
    Map<String, Object> getAllDeviceMonthPerformanceGrainMoveRate();
}
