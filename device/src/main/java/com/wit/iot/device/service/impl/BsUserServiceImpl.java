/**
 * @filename:BsUserServiceImpl 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2018 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.service.impl;

import com.wit.iot.device.domain.BsUser;
import com.wit.iot.device.mapper.BsUserDao;
import com.wit.iot.device.service.BsUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(API应用KEY服务实现)
 *
 * @version: V1.0
 * @author: miki
 * 
 */
@Service
public class BsUserServiceImpl extends ServiceImpl<BsUserDao, BsUser> implements BsUserService  {
	
}