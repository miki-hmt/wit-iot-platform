package com.witsoft.gongli.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.gongli.device.entity.DeviceMetricsLogEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceMetricsLogDao extends BaseMapper<DeviceMetricsLogEntity> {

    /**
     * 根据设备id取指定日期的设备指标数据
     */
    @Select("select id, device_id, availability, performance, quality, oee, create_time, good_count, bad_count, total_count, type from" +
            " device_metrics_log where device_id = #{deviceId} and to_char(create_time, 'yyyy-MM-dd') and type = #{type} order by create_time asc")
    public List<DeviceMetricsLogEntity> getMetricsLogList(String deviceId, Date date, String type);
}
