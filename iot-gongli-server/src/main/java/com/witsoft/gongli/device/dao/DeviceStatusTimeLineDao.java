package com.witsoft.gongli.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.witsoft.gongli.device.entity.DeviceStatusTimeLineEntity;
import com.witsoft.gongli.model.DeviceRealTimeSpent;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceStatusTimeLineDao extends BaseMapper<DeviceStatusTimeLineEntity> {

    /**
     * @desc 根据状态码获取当天的设备的最近一条记录
     * @param deviceId
     * @param status
     * @return
     */
    @Select("select id, device_id, status, start_time, end_time, event_time from device_status_timeline where device_id=#{deviceId} and" +
            " status = #{status} and to_char(start_time, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') order by start_time desc limit 1")
    DeviceStatusTimeLineEntity getLastOneInfoByStatus(String deviceId, String status);

    /**
     * @desc 获取当天的时序图设备列表
     * fixed(2021.11.09): 过滤时序图中状态为3，0的记录
     * @param deviceId
     * @return
     */
    @Select("select id, device_id, status, start_time, end_time, event_time from device_status_timeline where device_id=#{deviceId} and" +
            " to_char(start_time, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') and status not in ('3','0') order by start_time asc")
    List<DeviceStatusTimeLineEntity> getListByDeviceId(String deviceId);

    /**
     * @desc 取时间倒叙的第一条数据，以及倒数第一条数据
     * @param deviceId
     * @return
     */
    @Select("select id, device_id, status, start_time, end_time, event_time from device_status_timeline where device_id=#{deviceId} and" +
            " to_char(start_time, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') order by event_time desc limit 1")
    DeviceStatusTimeLineEntity getLastOneInfoByDeviceId(String deviceId);


    /**
     * @desc 取时间倒叙的第二条数据，以及倒数第二条数据
     * @param deviceId
     * @return
     */
    @Select("select id, device_id, status, start_time, end_time, event_time from device_status_timeline where device_id=#{deviceId} and" +
            " to_char(start_time, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') order by event_time desc limit 1,1")
    DeviceStatusTimeLineEntity getLastTwoStatusRecordByDeviceId(String deviceId);


    /**
     * @desc 取当日的运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Deprecated
    @Select("select coalesce(sum(date_part('hour', end_time - start_time) * 60 * 60 + date_part('minute', end_time - start_time) * 60 + " +
            "date_part('second', end_time - start_time)), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM-dd')=to_char(now(), 'yyyy-MM-dd') and status = '1'")
    Long getSumRunningTime(String deviceId);

    /**
     * @desc 取当日指定设备的运行时间时长（单位：秒）-v2 -查询方法优化
     * @param deviceId：设备id
     * @return
     */
    @Select("select COALESCE(sum(total_spent), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM-dd')=to_char(now(), 'yyyy-MM-dd') and status = '1'")
    Long getSumRunningTimeV2(String deviceId);


    /**
     * @desc 取当日所有设备的运行时长（单位：秒）-v2 -查询方法优化
     * @return
     */
    @Select("select COALESCE(sum(total_spent), 0) as seconds from device_status_timeline where " +
            "to_char(event_time, 'yyyy-MM-dd')=to_char(now(), 'yyyy-MM-dd') and status = '1'")
    Long getSumAllRunningTimeV2();


    /**
     * @desc 取当日所有设备的开机时长（单位：秒）-v2 -查询方法优化
     * @return
     */
    @Select("select COALESCE(sum(total_spent), 0) as seconds from device_status_timeline where " +
            "to_char(event_time, 'yyyy-MM-dd')=to_char(now(), 'yyyy-MM-dd') and status = '3'")
    Long getSumAllOpeningTimeV2();


    /**
     * @desc 取上个月的运行时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Deprecated
    @Select("select coalesce(sum(date_part('hour', end_time - start_time) * 60 * 60 + date_part('minute', end_time - start_time) * 60 + " +
            "date_part('second', end_time - start_time)), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM')=to_char(now() - interval '1' month, 'yyyy-MM') and status = '1'")
    Long getSumRunningTimeMonth(String deviceId);


    /**
     * @desc 取上个月的运行时间总数（单位：秒）-v2 -优化运行总时间查询效率
     * @param deviceId
     * @return
     */
    @Select("select COALESCE(sum(total_spent), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM')=to_char(now() - interval '1' month, 'yyyy-MM') and status = '1'")
    Long getSumRunningTimeMonthV2(String deviceId);

    /**
     * @desc 取上个月的开机时间总数（单位：秒）
     * @param deviceId
     * @return
     */
    @Select("select COALESCE(sum(total_spent), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM')=to_char(now() - interval '1' month, 'yyyy-MM') and status = '3'")
    Long getSumOpeningTimeMonth(String deviceId);

    /**
     * @desc 取设备当日的总时间（运行 + 待机 + 停机）（单位：秒）
     * @param deviceId
     * @return
     */
    @Select("select coalesce(sum(date_part('hour', end_time - start_time) * 60 * 60 + date_part('minute', end_time - start_time) * 60 + " +
            "date_part('second', end_time - start_time)), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM-dd')=to_char(now(), 'yyyy-MM-dd') and end_time is not null")
    Integer getSumTime(String deviceId);

    /**
     * @desc 取设备当日的总时间（运行 + 待机 + 停机）（单位：秒） -v2 sql查询优化
     * @param deviceId
     * @return
     */
    @Select("select coalesce(sum(total_spent), 0) as seconds from device_status_timeline where device_id=#{deviceId} " +
            "and to_char(event_time, 'yyyy-MM-dd')=to_char(now(), 'yyyy-MM-dd') and status IN('1','2','3')")
    Integer getSumTimeV2(String deviceId);


    /**
     * @desc 获取设备状态为Running，且total_spent为null的设备
     * @return
     */
    @Select("select device_id, status, start_time, total_spent, extract(epoch FROM (now() - start_time)) AS real_time_spent from " +
            "device_status_timeline where to_char(start_time, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') AND status = '1' AND total_spent IS NULL")
    List<DeviceRealTimeSpent> getSumAllRunningTimeDayOfTotalSpentIsnull();

    /**
     * @desc 获取设备状态为Turning，且total_spent为null的设备
     * @return
     */
    @Select("select device_id, status, start_time, total_spent, extract(epoch FROM (now() - start_time)) AS real_time_spent from " +
            "device_status_timeline where to_char(start_time, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') AND status = '3' AND total_spent IS NULL")
    List<DeviceRealTimeSpent> getSumAllOpeningTimeDayOfTotalSpentIsnull();

    /**
     * @desc 校验当前设备的total_spent为null时间后面存不存在不为空的，如果存在，不进行累加处理
     */
    @Select("select count(*) from device_status_timeline where status = #{status} and start_time > #{date} " +
            "and device_id = #{deviceId} and total_spent IS NOT NULL")
    Integer checkExistSpentIsNullAfter(Date date, String deviceId, String status);
}
