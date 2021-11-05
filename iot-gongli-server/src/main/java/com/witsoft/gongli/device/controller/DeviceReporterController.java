package com.witsoft.gongli.device.controller;

import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.device.service.DeviceReporterService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Api(tags = "设备月度报表统计接口", description = "设备信息报表")
@RestController
@RequestMapping("/iot/reporter")
public class DeviceReporterController {

    @Resource
    private DeviceReporterService reporterService;

    @Resource
    private DeviceStatusTimeLineService deviceStatusTimeLineService;


    @ApiOperation(value = "时间稼动率折线图")
    @GetMapping("/month/grainMoveRate")
    public Result getMonthReporterGrainMoveRate(){
        Map<String, Object> monthGrainMoveRate = reporterService.getAllDeviceMonthGrainMoveRate();
        return Result.success(monthGrainMoveRate);
    }

    @PostMapping("/save")
    public Result saveReporter(){
        Long sumOpeningTimeMonth = deviceStatusTimeLineService.getSumOpeningTimeMonth("4");
        DeviceReporterMonth deviceReporterMonth = new DeviceReporterMonth();
        deviceReporterMonth.setCreateTime(new Date());
        deviceReporterMonth.setDeviceId("4");
        deviceReporterMonth.setTimeGrainMoveRate(sumOpeningTimeMonth.toString());

        reporterService.save(deviceReporterMonth);
        return Result.success(true);
    }
}
