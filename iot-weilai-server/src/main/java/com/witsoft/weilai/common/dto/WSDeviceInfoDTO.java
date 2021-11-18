package com.witsoft.weilai.common.dto;

import lombok.Data;

/**
 * @date 2021.09.26
 * @Author miki
 * @desc 车间设备各种状态的总数统计
 */
@Data
public class WSDeviceInfoDTO {

    private String deviceState;
    private Integer deviceCount;
}
