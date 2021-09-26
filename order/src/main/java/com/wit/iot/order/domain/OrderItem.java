/**
 * @filename:OrderItem 2021年09月08日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.order.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

/**   
 * @Description:TODO(API应用KEY实体类)
 * 
 * @version: V1.0
 * @author: miki
 * 
 */
@TableName("order_item")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderItem extends Model<OrderItem> {

	private static final long serialVersionUID = 1631150712479L;
	
	@TableId(value = "id")
	private Long id;
    
	private Long orderId;
    
	private Long itemId;
    
	private Long itemVariationId;
    
	private Integer quantity;

	public OrderItem(Long id, Long orderId, Long itemId, Long itemVariationId, Integer quantity) {
		this.id = id;
		this.orderId = orderId;
		this.itemId = itemId;
		this.itemVariationId = itemVariationId;
		this.quantity = quantity;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
