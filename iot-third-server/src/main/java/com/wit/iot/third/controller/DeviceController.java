package com.wit.iot.third.controller;

import com.wit.iot.common.core.result.AjaxResult;
import com.wit.iot.device.domain.BsDeviceTelemetry;
import com.wit.iot.device.domain.vo.DeviceTelemetryVO;
import com.wit.iot.device.service.BsDeviceTelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private BsDeviceTelemetryService deviceTelemetryService;

    @GetMapping("/data")
    public AjaxResult getIncrementDate(DeviceTelemetryVO vo){
        Integer offset = vo.getOffset();
        if(null != offset && offset.equals(1)){
            vo.setOffset(0);
        }
        if(ObjectUtils.isEmpty(vo.getDataSize())){
            vo.setDataSize(100);
        }
        List<BsDeviceTelemetry> deviceTelemetries = deviceTelemetryService.selectDeviceList(vo);
        return AjaxResult.success(deviceTelemetries);
    }
}
