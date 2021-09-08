package com.wit.iot.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.iot.order.domain.Order;
import com.wit.iot.order.mapper.BsOrderDao;
import com.wit.iot.order.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<BsOrderDao, Order> implements OrderService {
}
