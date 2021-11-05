package com.wit.iot.mqtt.controller;


import com.wit.iot.mqtt.domain.User;
import com.wit.iot.mqtt.repository.UserDao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserDao userDao;

    @PostMapping("/create")
    public String add(@RequestBody User user){
        userDao.insert(user);

        return "OK";
    }
}
