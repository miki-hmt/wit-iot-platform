package com.witsoft.weilai.domain.iot;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.witsoft.weilai.enums.DeviceType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 设备属性
 **/
@Data
@TableName("device")
public class Device {
    /**
     * device_id
     */
    @TableId
    private Integer deviceId;
    /**
     * device_name
     * 名称
     */

    private String deviceName;
    /**
     * device_model
     * 设备型号
     */

    private String deviceModel;
    /**
     * device_label
     * 标签
     */

    private String deviceLabel;
    /**
     * device_state
     * 设备状态：0: 上电 1：运行 2：报警 3：关机
     */

    private String deviceState;
    /**
     * workshop_id
     * 车间id
     */

    private String workshopId;

    /**
     * create_date
     * 创建时间
     */
    private String createDate;

    /**
     * 设备类型
     */
    @TableField("device_type")
    private DeviceType deviceType;

    private String imgUrl;

    /**
     * @desc 启动日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date useDate;
    //出厂编号
    private String factoryNumber;

}
