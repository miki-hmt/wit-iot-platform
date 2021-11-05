package com.witsoft.gongli.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.gongli.device.dao.DeviceStatusTimeLineDao;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.device.entity.DeviceStatusTimeLineEntity;
import com.witsoft.gongli.device.enums.MachineStatusEnum;
import com.witsoft.gongli.device.service.DeviceService;
import com.witsoft.gongli.device.service.DeviceStatusTimeLineService;
import com.witsoft.gongli.model.DeviceStatus;
import com.witsoft.gongli.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

                //设备状态更新，加工产品数量数据更新
                if(!ObjectUtils.isEmpty(running)){
                    deviceEntity.setStatus(status.getCodeStr());
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
                    return;
                }

                //1-2：停机事件：更当天最近的一次开机事件，并计算出开机时间，更新到开机记录中
                if(status == MachineStatusEnum.STOPPING){
                    timeLineEntity.setStatus(MachineStatusEnum.STOPPING.getCodeStr());
                    this.save(timeLineEntity);

                    //获取今天最近的一次开机事件记录
                    DeviceStatusTimeLineEntity lastOneTURNING = getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.TURNING.getCodeStr());
                    if(!ObjectUtils.isEmpty(lastOneTURNING)){
                        Long leftSeconds = DateUtil.getLeftSeconds(lastOneTURNING.getStartTime(), new Date());
                        lastOneTURNING.setTotalSpent(leftSeconds);

                        //更新差值
                        this.saveOrUpdate(lastOneTURNING);
                        log.info("###########设备停机，更新当天最近一次status为 3 的时序记录：{}，记录主键为：{}", deviceName, lastOneTURNING.getId());
                    }

                    //如果不存在，今天设备记录为异常，不处理开机时间...
                    //...
                }

                //1-3：设备运行事件，新增时序记录
                if(MachineStatusEnum.RUNNING.getCodeStr().equals(running)){
                    timeLineEntity.setStatus(MachineStatusEnum.RUNNING.getCodeStr());
                    timeLineEntity.setEventTime(deviceStatus.getCreateDate());

                    this.save(timeLineEntity);
                    return;
                }

                //1-4：设备异常停机事件：新增时序记录，并更新当天上一条最近的RUNNING记录
                if(MachineStatusEnum.ERROR.getCodeStr().equals(running)){
                    timeLineEntity.setStatus(MachineStatusEnum.ERROR.getCodeStr());
                    timeLineEntity.setEventTime(deviceStatus.getCreateDate());

                    this.save(timeLineEntity);

                    //获取今天最近的一次设备运行事件记录
                    DeviceStatusTimeLineEntity lastOneRUNNING = getLastOneInfoByStatus(deviceEntity.getId(), MachineStatusEnum.RUNNING.getCodeStr());
                    if(ObjectUtils.isEmpty(lastOneRUNNING)){
                        //如果为空，今天人为异常，不处理
                        //...
                        return;
                    }

                    Long leftSeconds = DateUtil.getLeftSeconds(lastOneRUNNING.getStartTime(), new Date());
                    lastOneRUNNING.setTotalSpent(leftSeconds);

                    lastOneRUNNING.setEventTime(deviceStatus.getCreateDate());
                    lastOneRUNNING.setEndTime(deviceStatus.getCreateDate());
                    log.info("###########设备停机，更新当天最近一次status为 1 的时序记录：{}，记录主键为：{}", deviceName, lastOneRUNNING.getId());
                    //更新差值
                    this.saveOrUpdate(lastOneRUNNING);
                }
            }
        }catch (Exception e){
            log.error("###########设备时序记录上报异常：{}", e.getMessage());
        }
    }


    private DeviceEntity addDevice(DeviceStatus deviceStatus) {
        DeviceEntity deviceEntity = new DeviceEntity();

        deviceEntity.setBadCount(deviceStatus.getBadQuantity().longValue());
        deviceEntity.setGoodCount(deviceStatus.getGoodQuantity().longValue());
        //设备状态
        deviceEntity.setStatus(deviceStatus.getStatus().getCodeStr());
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
     * @desc 取当日的运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Override
    public Long getSumRunningTime(String deviceId) {
        return deviceStatusTimeLineDao.getSumRunningTime(deviceId);
    }

    /**
     * @desc 取上个月的设备运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Override
    public Long getSumRunningTimeMonth(String deviceId) {
        return deviceStatusTimeLineDao.getSumRunningTimeMonth(deviceId);
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
        return deviceStatusTimeLineDao.getSumTime(deviceId);
    }
}
