package com.witsoft.gongli.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.model.DeviceQuotaListInfo;
import com.witsoft.gongli.model.DeviceTotalInfo;
import com.witsoft.gongli.model.bo.DeviceQuotaBO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DeviceService extends IService<DeviceEntity> {

    List<DeviceEntity> getAllList();

    /**
     * @desc 调用mes接口，获取指定设备当天的设备节拍和有效产品数的乘积     --性能开动率计算公式 = 产品节拍数 * 报工数 / 设备运行总时间
     * @param deviceCodes
     * @param start
     * @param end
     * @param type
     * @return
     */
    Map<String, Double> getBeatMultiplyWorkReportOfDay(String deviceCodes, Date start, Date end, String type);

    /**
     * @desc 调用mes接口，获取指定设备当年12个月份的各个设备节拍和有效产品数的乘积     --性能开动率计算公式 = 产品节拍数 * 报工数 / 设备运行总时间
     * @param deviceCodes
     * @return
     */
    Map getBeatMultiplyWorkReportOfYear(String deviceCodes);

    /**
     * @desc 计算所有设备指标的oee,性能开动率，时间开动率
     * @return
     */
    DeviceQuotaListInfo getAverageDeviceTarget();

    DeviceQuotaBO getAverageDeviceTargetByDeviceId(String deviceId);

    DeviceEntity getInfo(String deviceId);

    DeviceEntity getDeviceByName(String name);

    List<DeviceTotalInfo> getDeviceTotalInfo();
}
