package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.dao.iot.DeviceFactoryRateDao;
import com.witsoft.weilai.domain.iot.DeviceFactoryRate;
import com.witsoft.weilai.service.iot.DeviceFactoryRateService;
import org.springframework.stereotype.Service;

@Service
public class DeviceFactoryRateServiceImpl extends ServiceImpl<DeviceFactoryRateDao, DeviceFactoryRate> implements DeviceFactoryRateService {
}
