package com.witsoft.weilai.dao.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.weilai.common.dto.StationRateDTO;
import com.witsoft.weilai.domain.iot.StationRate;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRateDao extends BaseMapper<StationRate> {

    StationRateDTO getStationInfo(Integer stationId);
}
