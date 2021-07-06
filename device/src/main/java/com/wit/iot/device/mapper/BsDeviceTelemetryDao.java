/**
 * @filename:BsDeviceTelemetryDao 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.iot.device.domain.BsDeviceTelemetry;
import com.wit.iot.device.domain.vo.DeviceTelemetryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**   
 * @Description:TODO(API应用KEY数据访问层)
 *
 * @version: V1.0
 * @author: miki
 * 
 */
@Mapper
public interface BsDeviceTelemetryDao extends BaseMapper<BsDeviceTelemetry> {

    /**
     * 增量查询接口
     */
    @Select("select * from bs_device_telemetry limit #{offset}, #{dataSize}")
    List<BsDeviceTelemetry> selectDeviceList(DeviceTelemetryVO vo);

    List<BsDeviceTelemetry> getIncrementData(DeviceTelemetryVO vo);
}
