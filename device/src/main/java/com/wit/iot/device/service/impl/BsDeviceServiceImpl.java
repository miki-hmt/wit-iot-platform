/**
 * @filename:BsDeviceServiceImpl 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2018 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.service.impl;

import com.wit.iot.device.domain.BsDevice;
import com.wit.iot.device.mapper.BsDeviceDao;
import com.wit.iot.device.service.BsDeviceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**   
 * @Description:TODO(API应用KEY服务实现)
 *
 * @version: V1.0
 * @author: miki
 * 
 */
@Service
public class BsDeviceServiceImpl extends ServiceImpl<BsDeviceDao, BsDevice> implements BsDeviceService{

    //暂时不加事务，影响插入性能   2021.06.20
//    @Transactional(rollbackFor = Exception.class, transactionManager = "deviceTransactionManager")
//    @Override
//    public boolean saveOrUpdate(BsDevice entity) {
//        return super.saveOrUpdate(entity);
//    }
//
//    @Transactional(rollbackFor = Exception.class, transactionManager = "deviceTransactionManager")
//    @Override
//    public boolean save(BsDevice entity) {
//        return super.save(entity);
//    }
}