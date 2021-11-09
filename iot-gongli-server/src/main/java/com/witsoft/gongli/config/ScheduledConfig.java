package com.witsoft.gongli.config;


import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.device.entity.DeviceStatusTimeLineEntity;
import com.witsoft.gongli.device.enums.MachineStatusEnum;
import com.witsoft.gongli.device.service.DeviceReporterService;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.witsoft.gongli.utils.DateUtil.sdf_s;

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
     * @desc 计算每天设备的指标，解决一些设备连续工作好几天，无法计算开机时长和运行时长的问题
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void calcMetricsOfDay(){
        //获取当前时间的前一秒
        Date yesterday = new Date(System.currentTimeMillis() - 1000);
        Date now = new Date();
        log.info("手动计算前一日设备运行和开机时间：{}", sdf_s.format(yesterday));

        try{
            List<DeviceEntity> allList = deviceService.getAllList();
            if(!CollectionUtils.isEmpty(allList)){
                List<DeviceStatusTimeLineEntity> add = new ArrayList<>();
                List<DeviceStatusTimeLineEntity> update = new ArrayList<>();

                for (DeviceEntity deviceEntity: allList) {
                    //步骤1：处理昨天的设备运行时长和开机时长
                    //1-1：构造停机事件
                    DeviceStatusTimeLineEntity stopEvent = new DeviceStatusTimeLineEntity();
                    stopEvent.setStatus(MachineStatusEnum.STOPPING.getCodeStr());
                    stopEvent.setDeviceId(deviceEntity.getId());
                    stopEvent.setId(UUID.randomUUID().toString());
                    stopEvent.setStartTime(yesterday);
                    add.add(stopEvent);

                    //1-2：更新表中昨天最近的running和turning事件
                    DeviceStatusTimeLineEntity runningEvent = timeLineService.getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.RUNNING.getCodeStr());
                    DeviceStatusTimeLineEntity turningEvent = timeLineService.getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.TURNING.getCodeStr());
                    //1-3：更新上一日运行时长
                    if(null != runningEvent){
                        Long leftSeconds = DateUtil.getLeftSeconds(runningEvent.getStartTime(), yesterday);
                        runningEvent.setTotalSpent(leftSeconds);
                        runningEvent.setEndTime(yesterday);
                        update.add(runningEvent);
                    }
                    //1-4：更新上一日开机时长
                    if(null != turningEvent){
                        Long leftSeconds = DateUtil.getLeftSeconds(turningEvent.getStartTime(), yesterday);
                        turningEvent.setTotalSpent(leftSeconds);
                        turningEvent.setEndTime(yesterday);
                        update.add(turningEvent);
                    }


                    //步骤2：处理今天的设备运行和开机事件
                    //2-1：将昨天手动停机的设备开机
                    //今日开机事件
                    DeviceStatusTimeLineEntity turnEvent = new DeviceStatusTimeLineEntity();
                    turnEvent.setStatus(MachineStatusEnum.TURNING.getCodeStr());
                    turnEvent.setDeviceId(deviceEntity.getId());
                    turnEvent.setId(UUID.randomUUID().toString());
                    turnEvent.setStartTime(now);
                    add.add(turnEvent);

                    //今日运行事件
                    DeviceStatusTimeLineEntity runningEventNow = new DeviceStatusTimeLineEntity();
                    runningEventNow.setStatus(MachineStatusEnum.RUNNING.getCodeStr());
                    runningEventNow.setDeviceId(deviceEntity.getId());
                    runningEventNow.setId(UUID.randomUUID().toString());
                    runningEventNow.setStartTime(now);
                    add.add(runningEventNow);
                }

                timeLineService.saveBatch(add);
                timeLineService.saveOrUpdateBatch(update);
            }
        }catch (Exception e){
            log.error("手动补充设备停机事件异常：{}", e.getMessage());
        }
    }

    /**
     * @desc 设备月度指标计算，每月的第一天执行上个月的指标报表统计
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void calcMetricsOfMonth(){
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


    /**
     * @desc 设备数据模拟
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void mockDeviceData(){
        log.info("#### 开始模拟设备数据");
    }
}
