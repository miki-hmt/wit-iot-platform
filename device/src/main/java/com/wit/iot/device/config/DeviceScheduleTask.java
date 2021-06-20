package com.wit.iot.device.config;

import com.wit.iot.device.service.BsDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName DeviceScheduleTask
 * @Description TODO
 * @Author miki
 * @Date 2021/6/20 22:53
 * @Version 1.0
 */
@Component
@Slf4j
public class DeviceScheduleTask {

    @Autowired
    private BsDeviceService bsDeviceService;


    //每天模拟定时生产数据，十秒产生一条设备数据
    @Scheduled(cron = "0/10 * * * * ?")
    private void timingProductData(){

    }
}
