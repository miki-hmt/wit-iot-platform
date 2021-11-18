package com.witsoft.weilai.controller.iot;


import com.witsoft.weilai.domain.iot.DeviceFactoryRate;
import com.witsoft.weilai.domain.iot.DeviceMaintenance;
import com.witsoft.weilai.enums.MaintenanceType;
import com.witsoft.weilai.service.iot.DeviceFactoryRateService;
import com.witsoft.weilai.service.iot.DeviceMaintenanceService;
import com.witsoft.weilai.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "厂房统计信息接口", tags="厂房详细指标数据")
@RestController
@RequestMapping("/factory")
public class FactoryController {

    @Resource
    private DeviceFactoryRateService factoryRateService;
    @Resource
    private DeviceMaintenanceService maintenanceService;


    @ApiOperation("厂房详细指标数据")
    @GetMapping("/info")
    public Result<DeviceFactoryRate> getInfo(@RequestParam(value = "factoryId", required = false) Integer factoryId){
        if(ObjectUtils.isEmpty(factoryId)){
            factoryId = 1;
        }
        DeviceFactoryRate factoryRate = factoryRateService.getById(factoryId);
        return Result.success(factoryRate);
    }


    @ApiOperation("厂房设备维修记录列表")
    @GetMapping("/maintenance/list")
    public Result getMaintenanceList(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "type", required = false) Integer type){

        MaintenanceType maintenanceType = MaintenanceType.getInstance(type);
        if(ObjectUtils.isEmpty(maintenanceType)){
            maintenanceType = MaintenanceType.FACTORY;
        }
        if(ObjectUtils.isEmpty(id)){
            id = 1;
        }

        List<DeviceMaintenance> deviceMaintenances = maintenanceService.getMaintenanceListByType(id, maintenanceType);
        return Result.success(deviceMaintenances);
    }
}
