/**
 * @filename:BsUser 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

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
public class BsUser extends Model<BsUser> {

	private static final long serialVersionUID = 1624201705871L;
	
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private String name;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
