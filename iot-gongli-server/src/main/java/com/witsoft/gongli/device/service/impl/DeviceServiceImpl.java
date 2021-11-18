package com.witsoft.gongli.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceDao;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.enums.Status;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.model.DeviceQuota;
import com.witsoft.gongli.model.DeviceQuotaListInfo;
import com.witsoft.gongli.model.DeviceTotalInfo;
import com.witsoft.gongli.model.bo.DeviceQuotaBO;
import com.witsoft.gongli.utils.DateUtil;
import com.witsoft.gongli.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceDao, DeviceEntity> implements DeviceService {

    @Resource
    private DeviceDao deviceDao;
    @Value("${gongli.mes.host}")
    private String host;

    @Value("${gongli.mes.performance-rate-api}")
    private String performanceApi;

    @Value("${gongli.mes.sign}")
    private String sign;
    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private DeviceStatusTimeLineService timeLineService;


    @Override
    public Map getBeatMultiplyWorkReportOfYear(String deviceCodes) {

        StringBuffer timeRanges = new StringBuffer();
        try{
            for (int i = 1; i <= 12; i++) {
                Long dayTimeStampOfMonth = DateUtil.getFirstDayTimeStampOfMonth(i);
                Long lastDayTimeStampOfMonth = DateUtil.getLastDayTimeStampOfMonth(i);

                timeRanges.append(dayTimeStampOfMonth).append("-").append(lastDayTimeStampOfMonth).append(",");
            }
        }catch (Exception e){
            log.error("日期处理异常：{}", e.getMessage());
            timeRanges.append("0").append("-").append("31").append(",");
        }

        String timeRangesStr = timeRanges.substring(0, timeRanges.length() - 1);
        String url = host + performanceApi +"?deviceCodes=" + deviceCodes +"&timeRange="+ timeRangesStr +"&sign=" +sign +"&timeType=month";
        Result result = restTemplate.getForObject(url, Result.class);

        log.info("### mes 调用结果：{}", JSONObject.toJSONString(result));
        if(result.getCode() == 200){
            Map data = (Map) result.getData();
            return data;
        }
        return new HashMap<>();
    }

    @Override
    public DeviceQuotaListInfo getAverageDeviceTarget() {

        DeviceQuotaListInfo deviceQuotaListInfo = new DeviceQuotaListInfo();
        List<DeviceQuota> list = new ArrayList<>();

        try{
            //double型数字格式化小数点
            DecimalFormat decimalFormat = new DecimalFormat("#");
            List<DeviceEntity> allList = this.getAllList();

            if(!CollectionUtils.isEmpty(allList)){
                Double sumQuantity = 0.0, sumPerformance = 0.0, sumAvailability = 0.0, sumOee = 0.0;
                Double quantityRate = 0.0, performanceRate = 0.0, availability = 0.0;

                //TODO 获取mes的所有设备的节拍数和有效产品数的乘积
                String deviceCodes = getDeviceCodes(allList);
                Date start = DateUtil.getCurrentZero(new Date());
                Map<String, Double> workReportOfDay = getBeatMultiplyWorkReportOfDay(deviceCodes, start, new Date(), "day");


                for (DeviceEntity deviceEntity: allList) {
                    DeviceQuota deviceQuota = new DeviceQuota();
                    Long totalCount = deviceEntity.getTotalCount();
                    Long goodCount = deviceEntity.getGoodCount();


                    //合格率(不保留小数)计算：合格数/总数量
                    deviceQuota.setQuantity("0");
                    if(totalCount > 0){
                        quantityRate = 1.0 *  goodCount / totalCount;
                        deviceQuota.setQuantity(decimalFormat.format(quantityRate * 100));
                    }

                    //性能开动率计算： 节拍/（运行总时长 / 总生产数）
                    //取当天的所有设备的运行时长（单位：秒）
                    //fixed(2021.11.15): 通过mes获取设备的节拍和报工数量的乘积
                    Double reportNum = workReportOfDay.get(deviceEntity.getName());
                    if(ObjectUtils.isEmpty(reportNum)){
                        reportNum = 0.0;
                    }

                    Long sumRunningTime = timeLineService.getSumRunningTimeDay(deviceEntity.getId());
                    if(sumRunningTime > 0){
                        //v2:
                        performanceRate = 1.0 * reportNum / sumRunningTime;
                        //v1:
                        //performanceRate = (1.0 * deviceMeter) / (sumRunningTime/totalCount);
                    }
                    deviceQuota.setPerformance(decimalFormat.format(performanceRate * 100));


                    //时间开动率：当天运行总时长(秒)/总时长
                    //取当天所有设备的总时长（运行+待机+停机 单位：秒）
                    Integer sumTime = timeLineService.getSumTime(deviceEntity.getId());
                    deviceQuota.setAvailability("0");
                    if(sumTime > 0){
                        availability = 1.0 * sumRunningTime / sumTime;
                        deviceQuota.setAvailability(decimalFormat.format(availability * 100));
                    }

                    //oee计算：
                    Double oee = quantityRate * performanceRate * availability;
                    deviceQuota.setOee(decimalFormat.format(oee * 100));

                    //所有设备总的指标累计计算：
                    sumQuantity += quantityRate;
                    sumPerformance += performanceRate;
                    sumAvailability += availability;
                    sumOee += oee;

                    //扫尾工作：
                    deviceQuota.setDeviceName(deviceEntity.getName());
                    deviceQuota.setSerialNumber(deviceEntity.getSerialNumber());
                    list.add(deviceQuota);
                }

                //封装所有设备的指标
                deviceQuotaListInfo.setAllQuantity( sumQuantity <= 0 ? "0" :
                        decimalFormat.format(sumQuantity / allList.size() * 100));

                deviceQuotaListInfo.setAllPerformance( sumPerformance <= 0 ? "0" :
                        decimalFormat.format(sumPerformance / allList.size() * 100));

                deviceQuotaListInfo.setAllAvailability( sumAvailability <= 0 ? "0" :
                        decimalFormat.format(sumAvailability / allList.size() * 100));

                deviceQuotaListInfo.setAllOee( sumOee <= 0 ? "0" :
                        decimalFormat.format(sumOee / allList.size() * 100));

                deviceQuotaListInfo.setList(list);
            }
        }catch (Exception e){
            log.error("获取设备平均指标异常：{}", e.getMessage());
            return null;
        }
        return deviceQuotaListInfo;
    }


    @Override
    public DeviceQuotaBO getAverageDeviceTargetByDeviceId(String deviceId) {

        DeviceEntity deviceEntity = getInfo(deviceId);
        if(ObjectUtils.isEmpty(deviceEntity)){
            return null;
        }

        DeviceQuotaBO deviceQuota = new DeviceQuotaBO();
        try{
            //double型数字格式化小数点
            DecimalFormat decimalFormat = new DecimalFormat("#");
            Long totalCount = deviceEntity.getTotalCount();
            Long goodCount = deviceEntity.getGoodCount();
            Double quantityRate = 0.0, performanceRate = 0.0, availability = 0.0;

            //合格率(不保留小数)计算：合格数/总数量
            deviceQuota.setQuantity(0.0);
            if(totalCount >= 0){
                quantityRate = 1.0 *  goodCount / totalCount;
                deviceQuota.setQuantity(quantityRate);
            }

            //性能开动率计算：
            //通过mes获取设备的节拍和报工数的乘积
            Date start = DateUtil.getCurrentZero(new Date());
            Map<String, Double> workReportOfDay = getBeatMultiplyWorkReportOfDay(deviceEntity.getName(), start, new Date(), "day");
            Double beatAndTotal = workReportOfDay.get(deviceEntity.getName());
            if(ObjectUtils.isEmpty(beatAndTotal)){
                beatAndTotal = 0.0;
            }
            //取当天的所有设备的运行时长（单位：秒）
            Long sumRunningTime = timeLineService.getSumRunningTimeDay(deviceEntity.getId());
            if(sumRunningTime > 0){
                performanceRate = 1.0 * beatAndTotal / sumRunningTime;
            }
            deviceQuota.setPerformance(performanceRate);

            //时间开动率：
            //取当天所有设备的运行时长（单位：秒）
            Integer sumTime = timeLineService.getSumTime(deviceEntity.getId());
            deviceQuota.setAvailability(0.0);
            if(sumTime > 0){
                availability = 1.0 * sumRunningTime / sumTime;
                deviceQuota.setAvailability(availability);
            }

            //oee计算：
            Double oee = quantityRate * performanceRate * availability;
            deviceQuota.setOee(oee);

            deviceQuota.setDeviceName(deviceEntity.getName());
            deviceQuota.setSerialNumber(deviceEntity.getSerialNumber());
        }catch (Exception e){
            log.error("设备指标查询异常：{}", e.getMessage());
            return null;
        }
        return deviceQuota;
    }

    private String getDeviceCodes(List<DeviceEntity> allList) {

        StringBuffer codes = new StringBuffer();
        allList.forEach(deviceEntity -> {
            codes.append(deviceEntity.getName()).append(",");
        });

        String deviceCodes = codes.substring(0, codes.length() - 1);
        return deviceCodes;
    }

    @Override
    public Map<String, Double> getBeatMultiplyWorkReportOfDay(String deviceCodes, Date start, Date end, String type) {
        try{
            String timeRangeParam = start.getTime() + "-" + end.getTime();
            String url = host + performanceApi +"?deviceCodes=" + deviceCodes +"&timeRange="+ timeRangeParam +"&sign=" +sign +"&timeType=" +type;
            Result result = restTemplate.getForObject(url, Result.class);

            log.info("### mes 调用结果：{}", JSONObject.toJSONString(result));
            if(result.getCode() == 200){
                Map<String, Double> data = (Map<String, Double>) result.getData();
                return data;
            }
        }catch (Exception e){
            log.error("### mes api调用失败：{}", e.getMessage());
        }
        return new HashMap<>();
    }

    @Override
    public List<DeviceEntity> getAllList() {
        return deviceDao.getAllList();
    }

    @Override
    public DeviceEntity getInfo(String deviceId) {
        return deviceDao.getDeviceInfo(deviceId);
    }

    @Override
    public DeviceEntity getDeviceByName(String name) {
        LambdaQueryWrapper<DeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceEntity::getName, name);

        List<DeviceEntity> deviceEntities = deviceDao.selectList(queryWrapper);
        if(deviceEntities.size() > 0)
            return deviceEntities.get(0);

        return null;
    }

    @Override
    public List<DeviceTotalInfo> getDeviceTotalInfo() {
        return deviceDao.getDeviceTotalInfo();
    }
}
