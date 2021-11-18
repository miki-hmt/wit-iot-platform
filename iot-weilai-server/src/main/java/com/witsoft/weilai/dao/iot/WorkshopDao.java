package com.witsoft.weilai.dao.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.weilai.common.dto.WSDeviceInfoDTO;
import com.witsoft.weilai.domain.iot.Workshop;

import java.util.List;

public interface WorkshopDao extends BaseMapper<Workshop> {

    List<Workshop> getWorkshopList();

    /**
     * @desc 获取车间详情
     * @return
     */
    WSDeviceInfoDTO getWSDeviceInfo(Integer workshopId);
}
