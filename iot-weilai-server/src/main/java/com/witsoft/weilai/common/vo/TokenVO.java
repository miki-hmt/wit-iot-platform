package com.witsoft.weilai.common.vo;

import com.witsoft.weilai.domain.SysUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenVO {

    private LocalDateTime loginTime; //登录时间
    private LocalDateTime expireTime; //过期时间
    private SysUser sysUser;  //用户属性

}
