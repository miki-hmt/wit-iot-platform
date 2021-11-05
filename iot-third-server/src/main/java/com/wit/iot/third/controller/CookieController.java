package com.wit.iot.third.controller;


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
        Cookie loginCookie = new Cookie("TGC","12345");
        loginCookie.setDomain("sharehoo.cn");
        loginCookie.setPath("/");
        response.addCookie(loginCookie);

        return AjaxResult.success();
    }
}
