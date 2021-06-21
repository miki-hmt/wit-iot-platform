package com.wit.iot.device.config;

import com.wit.iot.device.domain.BsDeviceTelemetry;
import com.wit.iot.device.service.BsDeviceService;
import com.wit.iot.device.service.BsDeviceTelemetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

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

    BsDeviceService bsDeviceService;
    BsDeviceTelemetryService telemetryService;

    public DeviceScheduleTask(BsDeviceService bsDeviceService, BsDeviceTelemetryService telemetryService) {
        this.telemetryService = telemetryService;
        this.bsDeviceService = bsDeviceService;
    }

    //每天模拟定时生产数据，十秒产生一条设备数据
    @Scheduled(cron = "0/10 * * * * ?")
    private void timingProductData(){
        BsDeviceTelemetry telemetry = initDeviceTelemetry();
        telemetryService.save(telemetry);
        log.info("新增一条设备信息：{}", telemetry.getId());
    }

    private BsDeviceTelemetry initDeviceTelemetry() {
        BsDeviceTelemetry telemetry = new BsDeviceTelemetry();
        //随机数1-10
        Random rand = new Random();
        int num = rand.nextInt(11) + 1;
        telemetry.setBadCount(num);

        telemetry.setCreatetime(new Date());

        //随机数1-100
        num = rand.nextInt(100) + 1;
        telemetry.setDeviceId(String.valueOf(num));

        //随机数1-1000
        num = rand.nextInt(100) + 1;
        telemetry.setGoodCount(1000 + num);

        telemetry.setState(1);

        return telemetry;
    }
}
