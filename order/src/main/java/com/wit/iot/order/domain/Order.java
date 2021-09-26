/**
 * @filename:Order 2021年09月08日
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
import java.util.Date;

/**   
 * @Description:TODO(API应用KEY实体类)
 * 
 * @version: V1.0
 * @author: miki
 */



@TableName(value = "`order`")	//使用``来解决表名是特殊关键字
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Order extends Model<Order> {

	public Order(Long orderId, Long sellerUserId, Long sellerStoreId, Long buyerUserIld, Date createTime, Long createUserId, Date lastModifyTime, Long lastModifyUserId, Long isDeleted) {
		this.orderId = orderId;
		this.sellerUserId = sellerUserId;
		this.sellerStoreId = sellerStoreId;
		this.buyerUserIld = buyerUserIld;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.lastModifyTime = lastModifyTime;
		this.lastModifyUserId = lastModifyUserId;
		this.isDeleted = isDeleted;
	}

	private static final long serialVersionUID = 1631087690957L;
	@TableId(value="order_id")
	private Long orderId;
    
	private Long sellerUserId;
    
	private Long sellerStoreId;
    
	private Long buyerUserIld;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
    
	private Long createUserId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date lastModifyTime;
    
	private Long lastModifyUserId;
    
	private Long isDeleted;
    

	@Override
    protected Serializable pkVal() {
        return this.orderId;
    }
}
