package com.witsoft.gongli.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceReporterDao;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.device.service.DeviceReporterService;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.model.RunAndOpenTimesAllMonth;
import com.witsoft.gongli.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class DeviceReporterServiceImpl extends ServiceImpl<DeviceReporterDao, DeviceReporterMonth> implements DeviceReporterService {

    @Resource
    private DeviceReporterDao reporterDao;
    @Resource
    private DeviceService deviceService;

    @Override
    public Map<String, Object> getAllDeviceMonthGrainMoveRate() {
        //不保留小数
        DecimalFormat df = new DecimalFormat("#");
        //保留两位小数
        //DecimalFormat df = new DecimalFormat("#.00");

        //当前月份的开机时长和运行时长
        Long allDevicesSumRunningMonth = reporterDao.getAllDevicesSumRunningMonth();
        Long allDevicesSumOpeningMonth = reporterDao.getAllDevicesSumOpeningMonth();
        List<String> xdatas = new ArrayList<>(12);
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


    @Override
    public Map<String, Object> getAllDeviceMonthPerformanceGrainMoveRate() {
        //不保留小数
        DecimalFormat df = new DecimalFormat("#");
        //保留两位小数
        //DecimalFormat df = new DecimalFormat("#.00");
        //当月月份
        Long allDevicesSumRunningMonth = reporterDao.getAllDevicesSumRunningMonth();
        List<String> xdatas = new ArrayList<>(12);
        List<String> ydatas = new ArrayList<>(12);

        List<DeviceEntity> allList = deviceService.getAllList();
        Map workReportOfYear = deviceService.getBeatMultiplyWorkReportOfYear(getDeviceCodes(allList));
        Map<String, Double> totalNumByMonth = getTotalNumByMonth(allList, workReportOfYear);

        //查询当前月之前的所有月份的设备信息
        List<RunAndOpenTimesAllMonth> andRunningMonthBefore = reporterDao.getAllDevicesSumOpeningAndRunningMonthBefore();
        for (RunAndOpenTimesAllMonth andOpenTimesAllMonth: andRunningMonthBefore) {

            Long runningTimes = andOpenTimesAllMonth.getRunningTimes();
            //获取当月所有设备的节拍数和有效产品数的乘积
            Double sum = totalNumByMonth.get(andOpenTimesAllMonth.getDate());
            //记录月份
            xdatas.add(andOpenTimesAllMonth.getDate());

            //非空判断
            if(ObjectUtils.isEmpty(sum)){
                ydatas.add("0");
                //跳过向下执行
                continue;
            }

            //正确计算当前月的每一个月的指标
            Double rate = 1.0 * sum / runningTimes;
            ydatas.add(df.format(rate * 100));
        }

        //计算当前月的
        String nowMonth = DateUtil.sdf.format(new Date());
        Double nowMonthSum = totalNumByMonth.get(nowMonth);
        if(allDevicesSumRunningMonth <= 0){
            ydatas.add("0");
        }else{
            Double rate = 1.0 * nowMonthSum / allDevicesSumRunningMonth;
            ydatas.add(df.format(rate * 100));
        }

        //ydatas 进行排序
        //fixed(2021.11.08): 按12个月份，填充12个数据
        if(ydatas.size() < 12){
            int size = ydatas.size() - 12;
            for (int i = 0; i < size; i++) {
                ydatas.add("");
            }
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("x", xdatas);
        data.put("y", ydatas);

        return data;
    }


    private String getDeviceCodes(List<DeviceEntity> allList) {

        StringBuffer codes = new StringBuffer();
        allList.forEach(deviceEntity -> {
            codes.append(deviceEntity.getName()).append(",");
        });

        String deviceCodes = codes.substring(0, codes.length() - 1);
        return deviceCodes;
    }

    private Map<String, Double> getTotalNumByMonth(List<DeviceEntity> allList, Map beatMultiplyWorkReportOfYear){
        HashMap<String, Double> data = new HashMap<>();

        allList.forEach(deviceEntity -> {
            //获取该设备12个月各个月总的节拍与有效生产产品总数乘积
            Object res = beatMultiplyWorkReportOfYear.get(deviceEntity.getName());
            if(!ObjectUtils.isEmpty(res)){
                Map result = (Map) res;
                //遍历每个月的节拍与有效生产产品总数的乘积
                result.keySet().forEach(key ->{
                    Double num = (Double) result.get(key);

                    //将每个月设备相同月份的num进行累加
                    if(!data.containsKey(key)){
                        data.put(key.toString(), num);
                    }else {
                        Double value = (Double) data.get(key);
                        value += num;
                        data.put(key.toString(), value);
                    }
                });
            }
        });

        return data;
    }
}
