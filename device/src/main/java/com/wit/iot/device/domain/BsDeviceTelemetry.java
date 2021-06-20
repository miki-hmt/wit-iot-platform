/**
 * @filename:BsDeviceTelemetry 2019年10月16日
 * @project wallet-sign  V1.0
 * Copyright(c) 2020 miki Co. Ltd. 
 * All right reserved. 
 */
package com.wit.iot.device.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
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
public class BsDeviceTelemetry extends Model<BsDeviceTelemetry> {

	private static final long serialVersionUID = 1624201296180L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "")
	private String deviceId;
    
	@ApiModelProperty(name = "goodCount" , value = "")
	private Integer goodCount;
    
	@ApiModelProperty(name = "badCount" , value = "")
	private Integer badCount;
    
	@ApiModelProperty(name = "state" , value = "")
	private Integer state;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createtime" , value = "")
	private Date createtime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
