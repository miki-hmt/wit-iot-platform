package com.witsoft.weilai.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.witsoft.weilai.common.vo.TokenVO;
import com.witsoft.weilai.domain.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: token工具类
 **/
public class TokenUtil {

    private final static int EXPIRE = 12;

    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");
        }
        return token;
    }

    /**
     * 创建token
     * @param sysUser
     * @return
     */
    public static Map<String,String> createToken(SysUser sysUser) {
        String salt = "WIT";
        TokenVO tokenVO = new TokenVO();
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = now.plusHours(EXPIRE);
        //保存到redis
        tokenVO.setLoginTime(now);
        tokenVO.setExpireTime(expireTime);
        sysUser.setPassword("");
        tokenVO.setSysUser(sysUser);
        String token = Md5Utils.string2Md5(sysUser.getUserId()+salt);
        Map<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("token", token);
        map.put("userInfo", JSON.toJSONString(tokenVO));
        return map;
    }


}
