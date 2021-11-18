package com.witsoft.weilai.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import com.witsoft.weilai.common.dto.DeviceWarningDTO;
import com.witsoft.weilai.domain.iot.DeviceWarning;

import java.util.List;

public interface DeviceWarningService extends IService<DeviceWarning> {

    List<DeviceWarningDTO> getAllList();
}
