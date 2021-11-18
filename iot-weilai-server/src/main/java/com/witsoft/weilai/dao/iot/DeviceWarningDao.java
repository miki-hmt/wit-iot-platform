package com.witsoft.weilai.dao.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.weilai.common.dto.DeviceWarningDTO;
import com.witsoft.weilai.domain.iot.DeviceWarning;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceWarningDao extends BaseMapper<DeviceWarning> {

    List<DeviceWarningDTO> getAllList();
}
