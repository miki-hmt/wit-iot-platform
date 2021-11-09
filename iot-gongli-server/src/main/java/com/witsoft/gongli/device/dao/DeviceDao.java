package com.witsoft.gongli.device.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.gongli.device.entity.DeviceEntity;
import com.witsoft.gongli.model.DeviceTotalInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDao extends BaseMapper<DeviceEntity> {

    @Select("select status, count(status) from device group by status")
    List<DeviceTotalInfo> getDeviceTotalInfo();

    @Select("select id, create_time, type, name, alias, location, model_number, serial_number, kpi_cala_period, device_meter," +
            "plan_start_time, plan_end_time, coalesce(good_count,0), coalesce(bad_count,0), coalesce(total_count,0), ideal_run_rate, sort, status from device")
    List<DeviceEntity> getAllList();

    @Select("select id, create_time, type, name, alias, location, model_number, serial_number, kpi_cala_period, device_meter," +
            "plan_start_time, plan_end_time, coalesce(good_count,0), coalesce(bad_count,0), coalesce(total_count,0), ideal_run_rate, sort, status from device where id = #{id}")
    DeviceEntity getDeviceInfo(String id);
}
