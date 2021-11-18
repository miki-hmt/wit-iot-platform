package com.witsoft.weilai.service.sys;

import com.witsoft.weilai.domain.SysUser;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 用户接口
 **/
public interface SysUserService {

    /**
     * 用户登录
     * @param loginname   登录账号
     * @param password    密码
     * @return
     */
    SysUser getLogin(String loginname, String password);

}
