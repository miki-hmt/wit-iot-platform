package com.wit.iot.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.iot.user.domain.User;
import com.wit.iot.user.mapper.UserDao;
import com.wit.iot.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService{
}
