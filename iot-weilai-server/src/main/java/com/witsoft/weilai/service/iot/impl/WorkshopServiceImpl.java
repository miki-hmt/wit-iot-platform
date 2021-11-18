package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.common.dto.WSDeviceInfoDTO;
import com.witsoft.weilai.dao.iot.WorkshopDao;
import com.witsoft.weilai.domain.iot.Workshop;
import com.witsoft.weilai.service.iot.WorkshopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkshopServiceImpl extends ServiceImpl<WorkshopDao, Workshop> implements WorkshopService {

    @Resource
    private WorkshopDao workshopDao;

    @Override
    public List<Workshop> getWorkshopList() {
        return workshopDao.getWorkshopList();
    }

    @Override
    public WSDeviceInfoDTO getWSDeviceInfo(Integer workshopId) {
        return workshopDao.getWSDeviceInfo(workshopId);
    }
}
