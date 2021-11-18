package com.witsoft.weilai.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.weilai.common.dto.StationRateDTO;
import com.witsoft.weilai.domain.iot.StationRate;

public interface StationRateService extends IService<StationRate> {

    public StationRateDTO getStationInfo(Integer stationId);
}
