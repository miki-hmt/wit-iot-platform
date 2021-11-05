package com.witsoft.gongli.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceDao;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.model.DeviceTotalInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceDao, DeviceEntity> implements DeviceService {

    @Resource
    private DeviceDao deviceDao;

    @Override
    public List<DeviceEntity> getAllList() {
        return deviceDao.getAllList();
    }

    @Override
    public DeviceEntity getInfo(String deviceId) {
        return deviceDao.getDeviceInfo(deviceId);
    }

    @Override
    public DeviceEntity getDeviceByName(String name) {
        LambdaQueryWrapper<DeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceEntity::getName, name);

        List<DeviceEntity> deviceEntities = deviceDao.selectList(queryWrapper);
        if(deviceEntities.size() > 0)
            return deviceEntities.get(0);

        return null;
    }

    @Override
    public List<DeviceTotalInfo> getDeviceTotalInfo() {
        return deviceDao.getDeviceTotalInfo();
    }
}
