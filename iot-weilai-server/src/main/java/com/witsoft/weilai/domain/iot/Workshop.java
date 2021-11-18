package com.witsoft.weilai.domain.iot;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: wit
 * @create 2021/11/16
 * @Description: 车间属性
 **/
@Data
@TableName("workshop")
public class Workshop {
    /**
     * workshop_id
     */
    @TableId
    private Integer workshopId;

    /**
     * workshop_name
     * 车间名称
     */
    @TableField
    private String workshopName;




}
