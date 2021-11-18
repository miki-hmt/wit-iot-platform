package com.witsoft.weilai.domain.iot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: wit
 * @create 2021/11/16
 * @Description: 车间属性
 **/
@Data
@TableName("workshop_rate")
public class WorkshopRate {
    /**
     * workshop_id
     */
    @TableId(type = IdType.AUTO)
    private Integer workshopId;
    /**
     * utilize_rate
     * 设备利用率
     */
    private String utilizeRate;
    /**
     * fault_rate
     * 设备故障率
     */
    private String faultRate;
    /**
     * nofault_time
     * 连续运行无故障时间
     */
    private String nofaultTime;
    /**
     * repair_time
     * 故障平均修复时间
     */
    private String repairTime;

}





