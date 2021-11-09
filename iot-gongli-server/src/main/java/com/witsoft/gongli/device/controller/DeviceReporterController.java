package com.witsoft.gongli.device.controller;

import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.device.service.DeviceReporterService;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.model.DeviceQuota;
import com.witsoft.gongli.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DecimalFormat;
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
    @Resource
    private DeviceService deviceService;

    @GetMapping("/test/getAverageDeviceTarget")
    public Result getDeviceAverageDeviceTarget(String deviceId){

        DecimalFormat decimalFormat = new DecimalFormat("#");
        DeviceEntity deviceEntity = deviceService.getInfo(deviceId);

        DeviceQuota deviceQuota = new DeviceQuota();
        Long totalCount = deviceEntity.getTotalCount();
        Long goodCount = deviceEntity.getGoodCount();

        //合格率(不保留小数)计算：合格数/总数量
        deviceQuota.setQuantity("0");
        Double quantityRate = 0.0;
        if(totalCount > 0){
            quantityRate = 1.0 *  goodCount / totalCount;
            deviceQuota.setQuantity(decimalFormat.format(quantityRate * 100));
        }

        //性能开动率计算： 节拍/（运行总时长 / 总生产数）
        //取当天的所有设备的运行时长（单位：秒）
        Long sumRunningTime = deviceStatusTimeLineService.getSumRunningTimeDay(deviceEntity.getId());
        Double performanceRate = 0.0;
        if(totalCount > 0 && sumRunningTime > 0){
            performanceRate = 1.0 * 2 / (sumRunningTime / totalCount);
        }
        deviceQuota.setPerformance(decimalFormat.format(performanceRate * 100));


        //时间开动率：当天运行总时长(秒)/总时长
        //取当天所有设备的总时长（运行+待机+停机 单位：秒）
        Integer sumTime = deviceStatusTimeLineService.getSumTime(deviceEntity.getId());
        Double availability = 0.0;
        deviceQuota.setAvailability("0");
        if(sumTime > 0){
            availability = 1.0 * sumRunningTime / sumTime;
            deviceQuota.setAvailability(decimalFormat.format(availability * 100));
        }

        //oee计算：
        Double oee = quantityRate * performanceRate * availability;
        deviceQuota.setOee(decimalFormat.format(oee * 100));

        return Result.success(deviceQuota);
    }

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
