package com.witsoft.gongli.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.gongli.device.entity.DeviceMetricsLogEntity;

import java.util.Date;
import java.util.List;

public interface DeviceMetricsLogService extends IService<DeviceMetricsLogEntity> {

    List<DeviceMetricsLogEntity> getMetricsLogList(String deviceId, Date date, String type);
}
