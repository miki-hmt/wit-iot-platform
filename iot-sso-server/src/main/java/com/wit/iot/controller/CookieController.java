package com.wit.iot.controller;


import com.wit.iot.common.core.result.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cookie")
public class CookieController {


    @GetMapping("/add")
    public AjaxResult operateCookie(HttpServletResponse response){

        //login.sharehoo.cn 二级域名用做登录服务，然后将token写入主域名下的cookie，各个子域名是共享主域名的cookie的
        Cookie loginCookie = new Cookie("TGC","12345");
        loginCookie.setDomain("sharehoo.cn");
        loginCookie.setPath("/");
        response.addCookie(loginCookie);

        return AjaxResult.success();
    }
}
