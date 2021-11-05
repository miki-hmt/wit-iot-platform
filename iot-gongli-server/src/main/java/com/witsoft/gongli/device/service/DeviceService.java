package com.witsoft.gongli.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.model.DeviceTotalInfo;

import java.util.List;

public interface DeviceService extends IService<DeviceEntity> {

    List<DeviceEntity> getAllList();

    DeviceEntity getInfo(String deviceId);

    DeviceEntity getDeviceByName(String name);

    List<DeviceTotalInfo> getDeviceTotalInfo();
}
