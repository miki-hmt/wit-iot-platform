package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.dao.iot.DeviceMaintenanceDao;
import com.witsoft.weilai.domain.iot.DeviceMaintenance;
import com.witsoft.weilai.enums.MaintenanceType;
import com.witsoft.weilai.service.iot.DeviceMaintenanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeviceMaintenanceServiceImpl extends ServiceImpl<DeviceMaintenanceDao, DeviceMaintenance> implements DeviceMaintenanceService {

    @Resource
    private DeviceMaintenanceDao maintenanceDao;


    @Override
    public List<DeviceMaintenance> getMaintenanceListByType(Integer id, MaintenanceType type) {

        LambdaQueryWrapper<DeviceMaintenance> queryWrapper = new LambdaQueryWrapper<>();
        switch (type){
            case FACTORY:
                queryWrapper.eq(DeviceMaintenance::getFactoryId, id);
            break;
            case LINE:
            case WORKSHOP:
                queryWrapper.eq(DeviceMaintenance::getWorkshopId, id);
            case DEVICE:
                queryWrapper.eq(DeviceMaintenance::getDeviceId, id);
        }

        queryWrapper.eq(DeviceMaintenance::getType, type.getCode());
        //展示未完成的
        queryWrapper.eq(DeviceMaintenance::getStatus, 0);

        List<DeviceMaintenance> deviceMaintenances = maintenanceDao.selectList(queryWrapper);
        return deviceMaintenances;
    }
}
