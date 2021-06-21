package com.wit.iot.third.controller;

import com.wit.iot.common.core.result.AjaxResult;
import com.wit.iot.device.domain.BsDevice;
import com.wit.iot.device.domain.BsDeviceTelemetry;
import com.wit.iot.device.service.BsDeviceService;
import com.wit.iot.device.service.BsDeviceTelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author miki
 * @Date 2021/6/20 17:02
 * @Version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final BsDeviceService deviceService;
    private final BsDeviceTelemetryService telemetryService;

    public TestController(BsDeviceService deviceService, BsDeviceTelemetryService telemetryService) {
        this.deviceService = deviceService;
        this.telemetryService = telemetryService;
    }

    @GetMapping("/{id}")
    public void test(@PathVariable Integer id){
        deviceService.getById(id);
    }


    @PostMapping("/insert")
    public AjaxResult insert(@RequestBody BsDevice bsDevice){
        boolean save = deviceService.save(bsDevice);

        BsDeviceTelemetry telemetry = new BsDeviceTelemetry();
        telemetry.setBadCount(5);
        telemetry.setCreatetime(new Date());
        telemetry.setDeviceId("1");
        telemetry.setGoodCount(4);
        telemetry.setState(1);

        telemetryService.save(telemetry);
        return AjaxResult.success(save);
    }

}
