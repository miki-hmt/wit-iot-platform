package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.dao.iot.DeviceRateDao;
import com.witsoft.weilai.domain.iot.DeviceRate;
import com.witsoft.weilai.service.iot.DeviceRateService;
import org.springframework.stereotype.Service;

@Service
public class DeviceRateServiceImpl extends ServiceImpl<DeviceRateDao, DeviceRate> implements DeviceRateService {
}
