/**
 * @filename:BsDeviceTelemetryServiceImpl 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2018 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.service.impl;

import com.wit.iot.device.domain.BsDeviceTelemetry;
import com.wit.iot.device.domain.vo.DeviceTelemetryVO;
import com.wit.iot.device.mapper.BsDeviceTelemetryDao;
import com.wit.iot.device.service.BsDeviceTelemetryService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**   
 * @Description:TODO(API应用KEY服务实现)
 *
 * @version: V1.0
 * @author: miki
 * 
 */
@Service
public class BsDeviceTelemetryServiceImpl extends ServiceImpl<BsDeviceTelemetryDao, BsDeviceTelemetry> implements BsDeviceTelemetryService{

    @Resource
    BsDeviceTelemetryDao deviceTelemetryDao;

    @Override
    public List<BsDeviceTelemetry> selectDeviceList(DeviceTelemetryVO vo) {
        return deviceTelemetryDao.selectDeviceList(vo);
    }

    @Override
    public List<BsDeviceTelemetry> getIncrementData(DeviceTelemetryVO vo) {
        return deviceTelemetryDao.getIncrementData(vo);
    }
}