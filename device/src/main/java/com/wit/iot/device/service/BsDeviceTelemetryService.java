/**
 * @filename:BsDeviceTelemetryService 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.service;

import com.wit.iot.device.domain.BsDeviceTelemetry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.iot.device.domain.vo.DeviceTelemetryVO;

import java.util.List;

/**
 * @Description:TODO(API应用KEY服务层)
 * @version: V1.0
 * @author: miki
 * 
 */
public interface BsDeviceTelemetryService extends IService<BsDeviceTelemetry> {

    /**
     * 增量查询接口
     */
    List<BsDeviceTelemetry> selectDeviceList(DeviceTelemetryVO vo);

    List<BsDeviceTelemetry> getIncrementData(DeviceTelemetryVO vo);
}