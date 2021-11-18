package com.witsoft.weilai.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.weilai.common.dto.WSDeviceInfoDTO;
import com.witsoft.weilai.domain.iot.Workshop;

import java.util.List;

public interface WorkshopService extends IService<Workshop> {

    List<Workshop> getWorkshopList();

    /**
     * @desc 获取车间详情
     * @return
     */
    WSDeviceInfoDTO getWSDeviceInfo(Integer workshopId);
}
