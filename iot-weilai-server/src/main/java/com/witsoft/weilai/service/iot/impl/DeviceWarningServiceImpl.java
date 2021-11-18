package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.common.dto.DeviceWarningDTO;
import com.witsoft.weilai.dao.iot.DeviceWarningDao;
import com.witsoft.weilai.domain.iot.DeviceWarning;
import com.witsoft.weilai.service.iot.DeviceWarningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeviceWarningServiceImpl extends ServiceImpl<DeviceWarningDao, DeviceWarning> implements DeviceWarningService {

    @Resource
    private DeviceWarningDao deviceWarningDao;

    @Override
    public List<DeviceWarningDTO> getAllList() {
        return deviceWarningDao.getAllList();
    }
}
