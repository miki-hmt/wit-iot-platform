/**
 * @filename:DeviceFactoryRate 2021年09月08日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.witsoft.weilai.domain.iot;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description:TODO(API应用KEY实体类)
 * 
 * @version: V1.0
 * @author: miki
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceFactoryRate extends Model<DeviceFactoryRate> {

	private static final long serialVersionUID = 1637131669890L;
	
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    //厂房id
	private Integer factoryId;
    //订单平均响应时间
	private String averageOrderRt;
    //订单平均完成时间
	private String averageOrderCt;
    //当日订单完成率
	private String dayCmpRateOrder;
    //客户满意度
	private String satisfaction;
    //当日生产数量
	private Integer dayProduceNum;
    //今日合格品数
	private Integer dayGoodNum;
    //今日不合格品数
	private Integer dayBadNum;
    //今日合格率
	private String goodRate;
    //voc排放 or 危化温度
	private String vocEmission;
    //噪音
	private String noise;
    //强光
	private String strongLight;
    //粉尘
	private String dust;
    //送货及时率
	private String timelyDeliveryRate;
    //质量返工率
	private String qualityReworkRate;
    //厂房设备数
	private Integer deviceTotal;
    //厂房设备可动率
	private String factoryDeviceMobility;
    //累计停线次数
	private String lineStopTotal;
    //累计停线时长
	private String lineStopTotalSpent;
    //在岗人数
	private Integer onlineWorkers;
    //请假人数
	private Integer offlineWorkers;
    //今日订单交付率
	private String dayOrderDeliveryRate;
    //今日交付及时率
	private String dayTimelyDeliveryRate;
    //今日发货及时率
	private String dayTimelySentRate;
    //当日达成率
	private String dayAchievementRate;
    //当日计划达成率
	private String dayPlanAchievementRate;
    //当月达成率
	private String monthAchievementRate;
    //当月计划达成率
	private String monthPlanAchievementRate;
    //当年达成率
	private String yearAchievementRate;
    //当年计划达成率
	private String yearPlanAchievementRate;
	//更新时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modifyTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
