package com.witsoft.weilai.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 用户属性
 **/
@Data
@TableName("sys_user")
public class SysUser {
    /**
     * user_id
     */
    @TableId
    private Integer userId;

    /**
     * username
     * 用户名称
     */
    @TableField("user_name")
    private String userName;
    /**
     * loginname
     * 登录账号
     */
    @NotEmpty(message = "登录名不能为空")
    @ApiModelProperty(required = true, value = "登录名")
    @TableField("login_name")
    private String loginName;
    /**
     * password
     * 密码
     */
    @TableField
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty(required = true, value = "密码")
    private String password;

    @TableField(exist=false) //不属于表字段属性
    private String token;

}
