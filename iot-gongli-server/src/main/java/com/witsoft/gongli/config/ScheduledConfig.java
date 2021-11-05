package com.witsoft.gongli.config;


import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.device.service.DeviceReporterService;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class ScheduledConfig {

    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceStatusTimeLineService timeLineService;
    @Resource
    private DeviceReporterService reporterService;

    /**
     * @desc 设备月度指标计算，每月的第一天执行上个月的指标报表统计
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void calacMetricsOfMonth(){
        log.info("#### 开始执行报表统计");
        List<DeviceEntity> allList = deviceService.getAllList();
        ArrayList<DeviceReporterMonth> reporterMonths = new ArrayList<>();
        if(!CollectionUtils.isEmpty(reporterMonths)){
            for (DeviceEntity deviceEntity: allList) {
                Long sumRunningTimeMonth = timeLineService.getSumRunningTimeMonth(deviceEntity.getId());
                Long sumOpeningTimeMonth = timeLineService.getSumOpeningTimeMonth(deviceEntity.getId());

                DeviceReporterMonth month = new DeviceReporterMonth(sumRunningTimeMonth, sumOpeningTimeMonth,
                        new Date(), deviceEntity.getId());
                reporterMonths.add(month);
            }
            reporterService.saveBatch(reporterMonths);
        }
    }
}
