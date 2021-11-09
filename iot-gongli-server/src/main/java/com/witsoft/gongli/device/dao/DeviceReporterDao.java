package com.witsoft.gongli.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.gongli.device.entity.DeviceReporterMonth;
import com.witsoft.gongli.model.RunAndOpenTimesAllMonth;
import com.witsoft.gongli.model.RunningTimesAllMonth;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceReporterDao extends BaseMapper<DeviceReporterMonth> {

    /**
     * @desc 获取当月所有的设备的开机时间
     * @return
     */
    @Select("select coalesce(sum(month_opening_time),0) from device_month_reporter where to_char(create_time, 'yyyy-MM')=to_char(now(), 'yyyy-MM')")
    Long getAllDevicesSumOpeningMonth();

    /**
     * @desc 获取所有的设备的当月的设备运行时间
     * @return
     */
    @Select("select coalesce(sum(month_running_time),0) from device_month_reporter where to_char(create_time, 'yyyy-MM')=to_char(now(), 'yyyy-MM')")
    Long getAllDevicesSumRunningMonth();

    /**
     * @desc 获取所有设备的当月之前的所有月份的设备运行时间，开机时间
     * @return
     */
    @Select("select * from (select coalesce(sum(month_opening_time),0), to_char(create_time, 'yyyy-MM') as date from device_month_reporter\n" +
            "group by to_char(create_time, 'yyyy-MM')) order by date")
    List<RunAndOpenTimesAllMonth> getAllDevicesSumOpeningAndRunningMonthBefore();

    /**
     * @desc 获取所有设备的当月之前的所有月份的设备开机时间
     * @return
     */
    @Select("select coalesce(sum(month_opening_time),0) from device_month_reporter where to_char(create_time, 'yyyy-MM')=to_char(now(), 'yyyy-MM')")
    List<RunningTimesAllMonth> getAllDevicesSumOpeningMonthBefore();

    /**
     * @desc 获取所有设备的当月之前的所有月份的设备运行时间
     * @return
     */
    @Select("select coalesce(sum(month_opening_time),0) from device_month_reporter where to_char(create_time, 'yyyy-MM')=to_char(now(), 'yyyy-MM')")
    List<RunningTimesAllMonth> getAllDevicesSumRunningMonthBefore();
}
