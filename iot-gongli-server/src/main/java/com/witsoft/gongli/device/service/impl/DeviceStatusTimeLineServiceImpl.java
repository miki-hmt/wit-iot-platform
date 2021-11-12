package com.witsoft.gongli.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceStatusTimeLineDao;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.entity.DeviceStatusTimeLineEntity;
import com.witsoft.gongli.device.enums.MachineStatusEnum;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.model.DeviceRealTimeSpent;
import com.witsoft.gongli.model.DeviceStatus;
import com.witsoft.gongli.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DeviceStatusTimeLineServiceImpl extends ServiceImpl<DeviceStatusTimeLineDao, DeviceStatusTimeLineEntity>
        implements DeviceStatusTimeLineService {

    @Resource
    private DeviceStatusTimeLineDao deviceStatusTimeLineDao;
    @Resource
    private DeviceService deviceService;

    private Map<String, String> devices = null;

    //读取设备内容到内存中
    @PostConstruct
    private void init(){
        //使用线程安全的hashMap
        //gongli项目最多19个设备
        devices = new ConcurrentHashMap<>(32);

        List<DeviceEntity> allList = deviceService.getAllList();
        allList.forEach(deviceEntity -> {
            devices.put(deviceEntity.getName(), deviceEntity.getId());
        });
    }

    /**
     * @desc 正常时序数据处理
     * @param obj
     */
    @Override
    public void reportStatusNoSplitFlow(String obj) {
        DeviceEntity deviceEntity = null;
        String lastStatus = "";

        try{
            DeviceStatus deviceStatus = JSONObject.parseObject(obj, DeviceStatus.class);
            String deviceName = deviceStatus.getDeviceName();
            if(ObjectUtils.isEmpty(deviceName)){
                return;
            }

            String running = deviceStatus.getRunning();
            String machinesOnline = deviceStatus.getMachinesOnline();
            MachineStatusEnum status = deviceStatus.getStatus();

            //设备不存在，则创建
            if(!devices.containsKey(deviceName)){
                log.info("###########设备不存在：{}，执行创建操作", deviceName);
                deviceEntity = addDevice(deviceStatus);
            }else{
                deviceEntity = deviceService.getDeviceByName(deviceName);
                lastStatus = deviceEntity.getStatus();

                //设备状态更新，加工产品数量数据更新
                if(!ObjectUtils.isEmpty(status)){
                    //fixed(2021.11.11): 修复设备状态优先级问题【设备事件：{"machineOnline":1, "running": 0或1} 两个状态是绑定在一个事件中， 当running为1的时候，优先展示1】
                    deviceEntity.setStatus(deviceStatus.getRunningStatus().getCodeStr());
                }

                deviceEntity.setBadCount(deviceStatus.getBadQuantity().longValue());
                deviceEntity.setGoodCount(deviceStatus.getGoodQuantity().longValue());
                deviceEntity.setTotalCount(deviceEntity.getBadCount() + deviceEntity.getGoodCount());
                deviceService.saveOrUpdate(deviceEntity);
            }

            //第一步：处理开机状态时序记录 -- 对于事件不带running和machineOnline的字段，不处理到时序表中  2021.11.05
            if(!ObjectUtils.isEmpty(running) || !ObjectUtils.isEmpty(machinesOnline)){
                log.info("############设备运行状态：{}", status);

                //初始化一条新的时序记录
                DeviceStatusTimeLineEntity timeLineEntity = new DeviceStatusTimeLineEntity();
                timeLineEntity.setId(UUID.randomUUID().toString());
                timeLineEntity.setDeviceId(deviceEntity.getId());
                timeLineEntity.setStartTime(deviceStatus.getCreateDate());

                //1-1： 开机事件：增加开机的时序记录
                if(status == MachineStatusEnum.TURNING){
                    timeLineEntity.setStatus(MachineStatusEnum.TURNING.getCodeStr());
                    this.save(timeLineEntity);

                    //处理关机时长
                    updateLastStoppingEvent(deviceStatus, deviceEntity);
                    //fixed(2021.11.11): 移除return，  >>产线环境瞎：{"machineOnline":1, "running": 0} 开机和（运行或待机）两个状态是同时一个事件传递的，需要向下接着处理running状态
                    //return;
                }

                //1-2：停机事件：更当天最近的一次开机事件，并计算出开机时间，更新到开机记录中
                if(status == MachineStatusEnum.STOPPING){
                    timeLineEntity.setStatus(MachineStatusEnum.STOPPING.getCodeStr());
                    timeLineEntity.setEventTime(deviceStatus.getCreateDate());
                    this.save(timeLineEntity);

                    //处理开机时长
                    updateLastTurningEvent(deviceStatus, deviceEntity);

                    //fixed(2021.11.10) ：如果停机之前，设备状态正常，则更新运行时长
                    if(MachineStatusEnum.RUNNING.getCodeStr().equals(lastStatus)){
                        updateLastRunningEvent(deviceStatus, deviceEntity);
                    }

                    return;
                }

                //1-3：设备运行事件，新增时序记录
                if(MachineStatusEnum.RUNNING.getCodeStr().equals(running)){
                    timeLineEntity.setStatus(MachineStatusEnum.RUNNING.getCodeStr());
                    timeLineEntity.setEventTime(deviceStatus.getCreateDate());

                    this.save(timeLineEntity);

                    //获取今天最近的待机事件记录
                    updateLastWaitingEvent(deviceStatus, deviceEntity);
                    return;
                }

                //1-4：设备异常停机事件：新增时序记录，并更新当天上一条最近的RUNNING记录

                //fixed:(工利项目设备异常状态按待机（空闲）处理)：设备没有异常状态，按待机状态处理  2021.11.11
                if(MachineStatusEnum.ERROR.getCodeStr().equals(running)){
                    timeLineEntity.setStatus(MachineStatusEnum.ERROR.getCodeStr());
                    timeLineEntity.setEventTime(deviceStatus.getCreateDate());

                    this.save(timeLineEntity);

                    //获取今天最近的一次设备运行事件记录
                    updateLastRunningEvent(deviceStatus, deviceEntity);
                }
            }
        }catch (Exception e){
            log.error("###########设备时序记录上报异常：{}", e.getMessage());
        }
    }


    private void updateLastStoppingEvent(DeviceStatus deviceStatus, DeviceEntity deviceEntity) {
        //获取今天最近的一次关机事件
        DeviceStatusTimeLineEntity lastOneInfoSTOPPING = getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.STOPPING.getCodeStr());

        //fixed(2021.11.11): 如果最近的stopping事件，已经标记了end_time, 不需要再重新处理
        if(!ObjectUtils.isEmpty(lastOneInfoSTOPPING) && ObjectUtils.isEmpty(lastOneInfoSTOPPING.getEndTime())){
            //计算时差
            Long leftSeconds = DateUtil.getLeftSeconds(lastOneInfoSTOPPING.getStartTime(), new Date());
            lastOneInfoSTOPPING.setTotalSpent(leftSeconds);

            //更新差值
            this.saveOrUpdate(lastOneInfoSTOPPING);
            log.info("###########设备开机，更新当天最近一次status为 4 的时序记录：{}，记录主键为：{}", deviceEntity.getName(), lastOneInfoSTOPPING.getId());
        }

        //如果为空，今天人为异常，不处理
        //...
    }


    private void updateLastRunningEvent(DeviceStatus deviceStatus, DeviceEntity deviceEntity) {
        //获取今天最近的一次关机事件
        DeviceStatusTimeLineEntity lastOneInfoRUNNING = getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.RUNNING.getCodeStr());

        //fixed(2021.11.11): 如果最近的running事件，已经标记了end_time, 不需要再重新处理
        if(ObjectUtils.isEmpty(lastOneInfoRUNNING) || !ObjectUtils.isEmpty(lastOneInfoRUNNING.getEndTime())){
            //如果为空，今天人为异常，不处理
            //...
            return;
        }

        //计算时差
        Long leftSeconds = DateUtil.getLeftSeconds(lastOneInfoRUNNING.getStartTime(), new Date());
        lastOneInfoRUNNING.setTotalSpent(leftSeconds);

        //fixed(修复时序图展示缺失问题需要结束事件)：完善时序图结束事件 2021.11.10
        lastOneInfoRUNNING.setEndTime(deviceStatus.getCreateDate());
        lastOneInfoRUNNING.setEventTime(deviceStatus.getCreateDate());
        log.info("###########设备待机，更新当天最近一次status为 1 的时序记录：{}，记录主键为：{}", deviceEntity.getName(), lastOneInfoRUNNING.getId());
        //更新差值
        this.saveOrUpdate(lastOneInfoRUNNING);
    }


    private void updateLastWaitingEvent(DeviceStatus deviceStatus, DeviceEntity deviceEntity) {
        //获取今天最近的一次关机事件
        DeviceStatusTimeLineEntity lastOneInfoWAITING = getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.WAITING.getCodeStr());

        //fixed(2021.11.11): 如果最近的waiting事件，已经标记了end_time, 不需要再重新处理
        if(ObjectUtils.isEmpty(lastOneInfoWAITING) || !ObjectUtils.isEmpty(lastOneInfoWAITING.getEndTime())){
            //如果为空，今天人为异常，不处理
            //...
            return;
        }

        //计算时差
        Long leftSeconds = DateUtil.getLeftSeconds(lastOneInfoWAITING.getStartTime(), new Date());
        lastOneInfoWAITING.setTotalSpent(leftSeconds);

        //fixed(修复时序图展示缺失问题需要结束事件)：完善时序图结束事件 2021.11.10
        lastOneInfoWAITING.setEndTime(deviceStatus.getCreateDate());
        lastOneInfoWAITING.setEventTime(deviceStatus.getCreateDate());
        log.info("###########设备运行，更新当天最近一次status为 2 的时序记录：{}，记录主键为：{}", deviceEntity.getName(), lastOneInfoWAITING.getId());
        //更新差值
        this.saveOrUpdate(lastOneInfoWAITING);
    }

    private void updateLastTurningEvent(DeviceStatus deviceStatus, DeviceEntity deviceEntity) {
        //获取今天最近的一次关机事件
        DeviceStatusTimeLineEntity lastOneInfoTURNING = getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.TURNING.getCodeStr());

        //fixed(2021.11.11): 如果最近的waiting事件，已经标记了end_time, 不需要再重新处理
        if(!ObjectUtils.isEmpty(lastOneInfoTURNING) && ObjectUtils.isEmpty(lastOneInfoTURNING.getEndTime())){
            //计算时差
            Long leftSeconds = DateUtil.getLeftSeconds(lastOneInfoTURNING.getStartTime(), new Date());
            lastOneInfoTURNING.setTotalSpent(leftSeconds);

            //fixed(修复时序图展示缺失问题需要结束事件)：完善时序图结束事件 2021.11.10
            lastOneInfoTURNING.setEndTime(deviceStatus.getCreateDate());
            lastOneInfoTURNING.setEventTime(deviceStatus.getCreateDate());

            //更新差值
            this.saveOrUpdate(lastOneInfoTURNING);
            log.info("###########设备停机，更新当天最近一次status为 3 的时序记录：{}，记录主键为：{}", deviceEntity.getName(), lastOneInfoTURNING.getId());
        }

        //如果为空，今天人为异常，不处理
        //...
    }

    private DeviceEntity addDevice(DeviceStatus deviceStatus) {
        DeviceEntity deviceEntity = new DeviceEntity();

        deviceEntity.setBadCount(deviceStatus.getBadQuantity().longValue());
        deviceEntity.setGoodCount(deviceStatus.getGoodQuantity().longValue());
        //设备状态
        deviceEntity.setStatus(deviceStatus.getStatus() != null ? deviceStatus.getStatus().getCodeStr() : null);
        deviceEntity.setTotalCount(deviceEntity.getBadCount() + deviceEntity.getGoodCount());
        deviceEntity.setCreateTime(new Date().getTime());

        deviceEntity.setName(deviceStatus.getDeviceName());

        deviceService.save(deviceEntity);
        //更新内存数据
        devices.put(deviceEntity.getName(), deviceEntity.getId());

        return deviceEntity;
    }

    /**
     * @desc 消息上报到mq
     * @param obj
     */
    @Override
    public void reportStatusSplitFlow(String obj) {

    }

    /**
     * @desc 根据状态码获取当天的设备的最近一条记录
     * @param deviceId : 设备id
     * @param status：状态
     */
    @Override
    public DeviceStatusTimeLineEntity getLastOneInfoByStatus(String deviceId, String status) {
        return deviceStatusTimeLineDao.getLastOneInfoByStatus(deviceId, status);
    }

    /**
     * @desc 根据状态获取当天的时序图指定设备列表
     * @param deviceId : 设备id
     * @return
     */
    @Override
    public List<DeviceStatusTimeLineEntity> getListByDeviceId(String deviceId) {
        return deviceStatusTimeLineDao.getListByDeviceId(deviceId);
    }

    /**
     * @desc 取时间倒叙的第一条数据，以及倒数第一条数据
     * @param deviceId
     * @return
     */
    @Override
    public DeviceStatusTimeLineEntity getLastOneInfoByDeviceId(String deviceId) {
        return deviceStatusTimeLineDao.getLastOneInfoByDeviceId(deviceId);
    }

    /**
     * @desc 取当日的运行时长（单位：秒）
     * @param deviceId
     * @return
     */
    @Override
    public Long getSumRunningTimeDay(String deviceId) {
        //v2：优化查询运行总时间sql 2021.11.08
        return deviceStatusTimeLineDao.getSumRunningTimeV2(deviceId);
        //return deviceStatusTimeLineDao.getSumRunningTime(deviceId);
    }

    /**
     * @desc 取当日的所有设备运行时长（单位：秒）
     * @return
     */
    @Override
    public Long getSumAllRunningTimeDay() {
        return deviceStatusTimeLineDao.getSumAllRunningTimeV2();
    }

    /**
     * @desc 取当日的所有设备开机时长（单位：秒）
     * @return
     */
    @Override
    public Long getSumAllOpeningTimeDay() {
        return deviceStatusTimeLineDao.getSumAllOpeningTimeV2();
    }

    /**
     * @desc 取上个月的设备运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Override
    public Long getSumRunningTimeMonth(String deviceId) {
        //v2 优化查询月度运行总时间的sql效率
        return deviceStatusTimeLineDao.getSumRunningTimeMonthV2(deviceId);
        //v1
        //return deviceStatusTimeLineDao.getSumRunningTimeMonth(deviceId);
    }

    /**
     * @desc 取上个月指定设备的开机时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Override
    public Long getSumOpeningTimeMonth(String deviceId) {
        return deviceStatusTimeLineDao.getSumOpeningTimeMonth(deviceId);
    }

    /**
     * @desc 取设备当日的总时间（运行 + 待机 + 停机）（单位：秒）
     * @param deviceId
     * @return
     */
    @Override
    public Integer getSumTime(String deviceId) {
        //v2：优化查询运行总时间sql  2021.11.08
        return deviceStatusTimeLineDao.getSumTimeV2(deviceId);
        //v1:
        //return deviceStatusTimeLineDao.getSumTime(deviceId);
    }


    @Override
    public Long getSumAllOpeningTimeDayOfTotalSpentIsnull() {
        List<DeviceRealTimeSpent> openingTimeDayOfTotalSpentIsnull = deviceStatusTimeLineDao.getSumAllOpeningTimeDayOfTotalSpentIsnull();
        Map<String, DeviceRealTimeSpent> openingTime = distinct(openingTimeDayOfTotalSpentIsnull);

        Double sumOpen = 0d, sumRunning = 0d;

        //计算总的运行时长
        for (String key : openingTime.keySet()) {
            DeviceRealTimeSpent spent = openingTime.get(key);

            //校验后面是否存在处理过的时序记录
            Integer num = deviceStatusTimeLineDao.checkExistSpentIsNullAfter(spent.getStartTime(),
                    spent.getDeviceId(), MachineStatusEnum.TURNING.getCodeStr());
            if(num <= 0){
                sumOpen += spent.getRealTimeSpent();
            }
        }
        return sumOpen.longValue();
    }

    @Override
    public Long getSumAllRunningTimeDayOfTotalSpentIsnull() {
        List<DeviceRealTimeSpent> runningTimeDayOfTotalSpentIsnull = deviceStatusTimeLineDao.getSumAllRunningTimeDayOfTotalSpentIsnull();
        Map<String, DeviceRealTimeSpent> runningTime = distinct(runningTimeDayOfTotalSpentIsnull);

        Double sumOpen = 0d, sumRunning = 0d;

        //计算总的运行时长
        for (String key : runningTime.keySet()) {
            DeviceRealTimeSpent spent = runningTime.get(key);

            //校验后面是否存在处理过的时序记录
            Integer num = deviceStatusTimeLineDao.checkExistSpentIsNullAfter(spent.getStartTime(),
                    spent.getDeviceId(), MachineStatusEnum.RUNNING.getCodeStr());
            if(num <= 0){
                sumRunning += spent.getRealTimeSpent();
            }
        }
        return sumRunning.longValue();
    }

    /**
     * @desc 去重数据
     * @param data
     * @return
     */
    private Map<String, DeviceRealTimeSpent> distinct(List<DeviceRealTimeSpent> data){
        Map<String, DeviceRealTimeSpent> map = new HashMap<>(data.size());
        data.forEach(deviceRealTimeSpent -> {
            String deviceId = deviceRealTimeSpent.getDeviceId();
            if(map.containsKey(deviceId)){
                DeviceRealTimeSpent old = map.get(deviceId);
                //优先取重复数据里面最新的
                if(old.getStartTime().before(deviceRealTimeSpent.getStartTime())){
                    map.put(deviceId, deviceRealTimeSpent);
                }
            }else {
                map.put(deviceId, deviceRealTimeSpent);
            }
        });
        return map;
    }
}
