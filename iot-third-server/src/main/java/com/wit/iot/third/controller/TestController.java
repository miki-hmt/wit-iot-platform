package com.wit.iot.third.controller;

import com.wit.iot.common.core.result.AjaxResult;
import com.wit.iot.device.domain.BsDevice;
import com.wit.iot.device.service.BsDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private BsDeviceService deviceService;


    @GetMapping("/{id}")
    public void test(@PathVariable Integer id){
        deviceService.getById(id);
    }


    @PostMapping("/insert")
    public AjaxResult insert(@RequestBody BsDevice bsDevice){
        boolean save = deviceService.save(bsDevice);

        return AjaxResult.success(save);
    }

}
