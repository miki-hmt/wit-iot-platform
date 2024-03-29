/**
 * @filename:BsDeviceDao 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.iot.device.domain.BsDevice;
import org.apache.ibatis.annotations.Mapper;

/**   
 * @Description:TODO(API应用KEY数据访问层)
 *
 * @version: V1.0
 * @author: miki
 * 
 */
@Mapper
public interface BsDeviceDao extends BaseMapper<BsDevice> {
	
}
