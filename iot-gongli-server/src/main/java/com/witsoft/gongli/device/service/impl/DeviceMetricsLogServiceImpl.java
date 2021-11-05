package com.witsoft.gongli.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceMetricsLogDao;
import com.witsoft.gongli.device.entity.DeviceMetricsLogEntity;
import com.witsoft.gongli.device.service.DeviceMetricsLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DeviceMetricsLogServiceImpl extends ServiceImpl<DeviceMetricsLogDao, DeviceMetricsLogEntity> implements DeviceMetricsLogService {

    @Resource
    private DeviceMetricsLogDao metricsLogDao;

    @Override
    public List<DeviceMetricsLogEntity> getMetricsLogList(String deviceId, Date date, String type) {
        return metricsLogDao.getMetricsLogList(deviceId, date, type);
    }
}
