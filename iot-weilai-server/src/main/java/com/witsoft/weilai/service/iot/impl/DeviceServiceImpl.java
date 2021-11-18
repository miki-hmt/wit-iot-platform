package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.dao.iot.DeviceDao;
import com.witsoft.weilai.domain.iot.Device;
import com.witsoft.weilai.service.iot.DeviceService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceDao, Device> implements DeviceService {
}
