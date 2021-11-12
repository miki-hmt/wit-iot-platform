package com.witsoft.gongli.device.controller;


import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.enums.MachineStatusEnum;
import com.witsoft.gongli.device.enums.Status;
import com.witsoft.gongli.device.service.DeviceMetricsLogService;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.model.DeviceQuota;
import com.witsoft.gongli.model.DeviceQuotaListInfo;
import com.witsoft.gongli.model.DeviceTotalInfo;
import com.witsoft.gongli.model.vo.EchartVO;
import com.witsoft.gongli.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/iot/device")
@Api(tags="设备详情接口")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceStatusTimeLineService timeLineService;
    @Resource
    private DeviceMetricsLogService deviceMetricsLogService;

    @Value("${spring.rabbitmq.runMq}")
    private Boolean runMq;

    //性能开动率，标准节拍（配置参数值，可由mes传过来）/ （运行事件/生产产量） * 100%
    @Value("${gongli.device.setPerformance}")
    private Integer setPerformance;


    @GetMapping
    @ApiOperation(value = "设备列表")
    public Result list(){
        List<DeviceEntity> allList = deviceService.getAllList();

        return Result.success(allList);
    }

    @GetMapping("/getAverageDeviceTarget")
    @ApiOperation(value = "所有设备平均指标")
    public Result getAverageDeviceTarget(){
        DeviceQuotaListInfo deviceQuotaListInfo = new DeviceQuotaListInfo();
        List<DeviceQuota> list = new ArrayList<>();

        try{
            //double型数字格式化小数点
            DecimalFormat decimalFormat = new DecimalFormat("#");
            List<DeviceEntity> allList = deviceService.getAllList();

            if(!CollectionUtils.isEmpty(allList)){
                Double sumQuantity = 0.0, sumPerformance = 0.0, sumAvailability = 0.0, sumOee = 0.0;

                for (DeviceEntity deviceEntity: allList) {
                    //fixed（2021.11.08优先取数据库配置）：设备节拍
                    Double deviceMeter = deviceEntity.getDeviceMeter();
                    if(ObjectUtils.isEmpty(deviceMeter)){
                        deviceMeter = setPerformance.doubleValue();
                    }
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
                    Long sumRunningTime = timeLineService.getSumRunningTimeDay(deviceEntity.getId());
                    Double performanceRate = 0.0;
                    if(totalCount > 0 && sumRunningTime > 0){
                        performanceRate = 1.0 * deviceMeter / (sumRunningTime / totalCount);
                    }
                    deviceQuota.setPerformance(decimalFormat.format(performanceRate * 100));


                    //时间开动率：当天运行总时长(秒)/总时长
                    //取当天所有设备的总时长（运行+待机+停机 单位：秒）
                    Integer sumTime = timeLineService.getSumTime(deviceEntity.getId());
                    Double availability = 0.0;
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
            return Result.error(Status.ERROR);
        }
        return Result.success(deviceQuotaListInfo);
    }


    @ApiOperation("单个设备指标的计算")
    @GetMapping("/getDeviceMetricsLog")
    public Result getDeviceMetricsLog(String deviceId){
        DeviceQuota deviceQuota = new DeviceQuota();
        try{
            //double型数字格式化小数点
            DecimalFormat decimalFormat = new DecimalFormat("#");
            DeviceEntity deviceEntity = deviceService.getInfo(deviceId);
            Long totalCount = deviceEntity.getTotalCount();
            Long goodCount = deviceEntity.getGoodCount();

            //合格率(不保留小数)计算：合格数/总数量
            deviceQuota.setQuantity("0");
            Double quantityRate = 0.0;
            if(totalCount >= 0){
                quantityRate = 1.0 *  goodCount / totalCount;
                deviceQuota.setQuantity(decimalFormat.format(quantityRate * 100));
            }

            //性能开动率计算：
            //取当天的所有设备的运行时长（单位：秒）
            Long sumRunningTime = timeLineService.getSumRunningTimeDay(deviceEntity.getId());
            Double performanceRate = 0.0;
            if(totalCount > 0 && sumRunningTime > 0){
                performanceRate = 1.0 * setPerformance / (sumRunningTime / totalCount);
            }
            deviceQuota.setPerformance(decimalFormat.format(performanceRate * 100));

            //时间开动率：
            //取当天所有设备的运行时长（单位：秒）
            Integer sumTime = timeLineService.getSumTime(deviceEntity.getId());
            Double availability = 0.0;
            deviceQuota.setAvailability("0");
            if(sumTime > 0){
                availability = 1.0 * sumRunningTime / sumTime;
                deviceQuota.setAvailability(decimalFormat.format(availability * 100));
            }

            //oee计算：
            Double oee = quantityRate * performanceRate * availability;
            deviceQuota.setOee(decimalFormat.format(oee * 100));
        }catch (Exception e){
            log.error("设备指标查询异常：{}", e.getMessage());
            return Result.error(Status.DEVICE_INDEX_FAILED);
        }
        return Result.success(deviceQuota);
    }


    //{"badQuantity":0, "createTime":"1636095600000", "deviceName":"2", "goodQuantity":0,"running":1, "machineOnline": 1}
    @ApiOperation(value = "thingsboard平台上报设备数据接口")
    @PostMapping("/reportStatus")
    public Result reportStatus(@RequestBody @Valid String obj){
        log.info("设备运行状态变化，上传数据：{}", obj);

        if(runMq){
            timeLineService.reportStatusSplitFlow(obj);
        }else {
            timeLineService.reportStatusNoSplitFlow(obj);
        }

        return Result.success(Status.SUCCESS);
    }


    @ApiOperation(value = "车间所有设备状态分布饼状图")
    @GetMapping("/deviceTotalInfo")
    public Result deviceStatusPieChart(){
        Integer sum = 0;
        Integer openSum = 0;

        DecimalFormat decimalFormat = new DecimalFormat("#");
        ArrayList<EchartVO> objects = new ArrayList<>();
        DeviceTotalInfo deviceTotalInfo = new DeviceTotalInfo(sum, objects);

        List<DeviceTotalInfo> deviceTotalInfos = deviceService.getDeviceTotalInfo();
        for (DeviceTotalInfo totalInfo: deviceTotalInfos) {
            sum += totalInfo.getCount();
            MachineStatusEnum instance = MachineStatusEnum.getInstance(totalInfo.getStatus());

            EchartVO echartVO = new EchartVO(instance.getMsg(), totalInfo.getCount());
            echartVO.setStatus(totalInfo.getStatus().toString());

            if(instance == MachineStatusEnum.STOPPING){
                echartVO.setName(instance.getAlias());
            }
            //fixed（2121.11.08）:开机数量 = 运行数量 + 待机数量
            if(instance == MachineStatusEnum.RUNNING || instance == MachineStatusEnum.WAITING){
                openSum = openSum + deviceTotalInfo.getCount();
            }

            if(instance == MachineStatusEnum.ERROR){
                deviceTotalInfo.setErrorNum(deviceTotalInfo.getCount());
            }
            objects.add(echartVO);
        }

        deviceTotalInfo.setTotal(sum);
        deviceTotalInfo.setRunningNum(openSum);

        //v2：
        //处理当日设备稼动率：= 所有设备当日运行时间 / 所有设备开机时间
        deviceTotalInfo.setGrainMoveRate("0");
        Long sumAllOpeningTimeDay = timeLineService.getSumAllOpeningTimeDay();
        Long sumAllRunningTimeDay = timeLineService.getSumAllRunningTimeDay();

        //fixed（2021.11.11）:获取当日连续运行和开机设备（没用标记total_spent值的记录）的时长
        Long totalOpeningSpentIsnull = timeLineService.getSumAllOpeningTimeDayOfTotalSpentIsnull();
        Long totalRunningSpentIsnull = timeLineService.getSumAllRunningTimeDayOfTotalSpentIsnull();
        log.info("统计的连续运行的设备实时开机时间：{}，连续运行的设备实时运行时间：{}", totalOpeningSpentIsnull, totalRunningSpentIsnull);

        //累加
        sumAllOpeningTimeDay += totalOpeningSpentIsnull;
        sumAllRunningTimeDay += totalRunningSpentIsnull;

        if(sumAllOpeningTimeDay > 0){
            Double rate = 100 * 1.0 * sumAllRunningTimeDay / sumAllOpeningTimeDay;
            deviceTotalInfo.setGrainMoveRate(decimalFormat.format(rate));
        }

        //v1：
        //处理设备稼动率：= 时间开动率 * 性能开动率 * 合格频率
        /*Result averageDeviceTarget = getAverageDeviceTarget();
        if(averageDeviceTarget.isSuccess()){
            DeviceQuotaListInfo data = (DeviceQuotaListInfo) averageDeviceTarget.getData();
            int performance = Integer.parseInt(data.getAllPerformance());
            int availability = Integer.parseInt(data.getAllAvailability());
            int quantity = Integer.parseInt(data.getAllQuantity());
            //都是乘以100后的整数，这里需要还原回去变成百分比
            deviceTotalInfo.setGrainMoveRate(String.valueOf(performance * availability * quantity /10000));
        }*/

        //处理饼图
        objects.forEach(echartVO -> {
            Integer value = echartVO.getValue();
            Double rate = 1.0 * value / deviceTotalInfo.getTotal() * 100;
            echartVO.setValue(Integer.parseInt(decimalFormat.format(rate)));
        });

        return Result.success(deviceTotalInfo);
    }
}
