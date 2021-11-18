package com.witsoft.weilai.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.weilai.domain.iot.DeviceMaintenance;
import com.witsoft.weilai.enums.MaintenanceType;

import java.util.List;

public interface DeviceMaintenanceService extends IService<DeviceMaintenance> {

    /**
     * @desc 根据类型不同，查询相应的维修信息
     * @param id ：车间id，厂房id，设备id，流水线id
     * @param type
     * @return
     */
    List<DeviceMaintenance> getMaintenanceListByType(Integer id, MaintenanceType type);
}
