package com.witsoft.weilai.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.witsoft.weilai.common.dto.StationRateDTO;
import com.witsoft.weilai.dao.iot.StationRateDao;
import com.witsoft.weilai.domain.iot.StationRate;
import com.witsoft.weilai.service.iot.StationRateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StationRateServiceImpl extends ServiceImpl<StationRateDao, StationRate> implements StationRateService {

    @Resource
    private StationRateDao stationRateDao;

    @Override
    public StationRateDTO getStationInfo(Integer stationId){
        return stationRateDao.getStationInfo(stationId);
    }
}
