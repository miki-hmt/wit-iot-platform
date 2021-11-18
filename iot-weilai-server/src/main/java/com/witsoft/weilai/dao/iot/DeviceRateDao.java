package com.witsoft.weilai.dao.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.weilai.domain.iot.DeviceRate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRateDao extends BaseMapper<DeviceRate> {

    List<DeviceRate> list();
}
