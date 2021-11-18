package com.witsoft.weilai.interceptor;

import com.alibaba.fastjson.JSON;
import com.witsoft.weilai.manager.redis.RedisService;
import com.witsoft.weilai.utils.HttpContextUtil;
import com.witsoft.weilai.utils.ResponseResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 拦截器
 **/
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        String token = TokenUtil.getRequestToken(request);
//        if (StringUtils.isBlank(token)) {
//            setReturn(response, 4008, "用户未登录，请先登录");
//            return false;
//        }
//        //1. 根据token，查询用户信息
//        Object tokenValue = redisService.get(token);
//        //2. 若用户不存在,
//        if (tokenValue == null) {
//            setReturn(response, 400, "用户不存在");
//            return false;
//        }
//        //3. token失效
//        TokenVO tokenVO = JSON.parseObject(tokenValue.toString(),TokenVO.class);
//        if (tokenVO.getExpireTime().isBefore(LocalDateTime.now())) {
//            setReturn(response, 400, "用户登录凭证已失效，请重新登录");
//            return false;
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    private static void setReturn(HttpServletResponse response, int status, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtil.getOrigin());
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setStatus(400);
        response.setContentType("application/json;charset=utf-8");
        ResponseResult build = ResponseResult.build(status, msg);
        String json = JSON.toJSONString(build);
        httpResponse.getWriter().print(json);
    }

}
