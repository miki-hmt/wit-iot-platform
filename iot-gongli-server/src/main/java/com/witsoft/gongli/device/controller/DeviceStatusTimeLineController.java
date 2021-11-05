package com.witsoft.gongli.device.controller;

import com.witsoft.gongli.device.entity.DeviceStatusTimeLineEntity;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@Api(tags="设备时序图接口详情", description = "设备时序状态详情")
@RestController
@RequestMapping("/iot/deviceStatus")
public class DeviceStatusTimeLineController {

    @Autowired
    private DeviceStatusTimeLineService timeLineService;


    @GetMapping("/sequence")
    @ApiOperation(value = "获取当天设备的状态时序图")
    public Result list(String deviceId){
        List<DeviceStatusTimeLineEntity> listByDeviceId = timeLineService.getListByDeviceId(deviceId);

        return Result.success(listByDeviceId);
    }
}
