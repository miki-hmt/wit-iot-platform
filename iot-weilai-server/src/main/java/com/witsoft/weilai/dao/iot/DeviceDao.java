package com.witsoft.weilai.dao.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.weilai.domain.iot.Device;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDao extends BaseMapper<Device> {
}
