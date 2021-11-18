package com.witsoft.weilai.common;

import com.alibaba.fastjson.JSON;
import com.witsoft.weilai.common.vo.TokenVO;
import com.witsoft.weilai.domain.SysUser;
import com.witsoft.weilai.manager.redis.RedisService;
import com.witsoft.weilai.utils.TokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: wit
 * @create 2021/9/15
 * @Description: base控制器
 **/
public class BaseController {

    @Resource
    public RedisService redisService;
    @Resource
    public HttpServletRequest request;

    /**
     * 获取用户信息
     * @return
     */
    public SysUser getUserInfo(){
        String token = TokenUtil.getRequestToken(request);
        TokenVO tokenVO = JSON.parseObject(redisService.get(token).toString(), TokenVO.class);
        return tokenVO.getSysUser();
    }

}
