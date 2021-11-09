package com.witsoft.gongli.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceReporterDao;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.device.service.DeviceReporterService;
import com.witsoft.gongli.model.RunAndOpenTimesAllMonth;
import com.witsoft.gongli.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class DeviceReporterServiceImpl extends ServiceImpl<DeviceReporterDao, DeviceReporterMonth> implements DeviceReporterService {

    @Resource
    private DeviceReporterDao reporterDao;

    @Override
    public Map<String, Object> getAllDeviceMonthGrainMoveRate() {
        //不保留小数
        DecimalFormat df = new DecimalFormat("#");
        //保留两位小数
        //DecimalFormat df = new DecimalFormat("#.00");

        //当前月份的开机时长和运行时长
        Long allDevicesSumRunningMonth = reporterDao.getAllDevicesSumRunningMonth();
        Long allDevicesSumOpeningMonth = reporterDao.getAllDevicesSumOpeningMonth();
        List<String> xdatas = new ArrayList<>();
        List<String> ydatas = new ArrayList<>(12);

        List<RunAndOpenTimesAllMonth> andRunningMonthBefore = reporterDao.getAllDevicesSumOpeningAndRunningMonthBefore();
        for (RunAndOpenTimesAllMonth allMonth: andRunningMonthBefore) {
            Long openTimes = allMonth.getOpenTimes();
            Long runningTimes = allMonth.getRunningTimes();
            //记录月份
            xdatas.add(allMonth.getDate());

            //非空判断
            if(openTimes <= 0){
                ydatas.add("0");
                //跳过当前循环
                continue;
            }

            //正确计算当月之前的每一个月的指标(求整数，需要将百分数*100)
            Double rate = 100 * 1.0 * openTimes / runningTimes;
            ydatas.add(df.format(rate));
        }

        //定时任务每月一号执行统计上个月的报表任务（统计不到当月的），所以这里要单独统计当月的报表指标
        xdatas.add(DateUtil.sdf.format(new Date()));
        //计算当月指标
        if(allDevicesSumOpeningMonth <= 0){
            ydatas.add("0");
        }else{
            Double rate = 100 * 1.0 * allDevicesSumRunningMonth / allDevicesSumOpeningMonth;
            ydatas.add(df.format(rate));
        }

        //fixed:(2021.11.08) 按12个月份，填充12个数据
        if(ydatas.size() < 12){
            int size = 12 - ydatas.size();
            for (int i = 0; i < size; i++) {
                ydatas.add("");
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("x", xdatas);
        data.put("y", ydatas);

        return data;
    }
}
