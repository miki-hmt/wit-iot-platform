package com.wit.iot.third.test.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.iot.item.domain.Item;
import com.wit.iot.item.service.ItemService;
import com.wit.iot.order.domain.Order;
import com.wit.iot.order.domain.OrderItem;
import com.wit.iot.order.manager.handler.HandlerSubmit;
import com.wit.iot.order.service.OrderItemService;
import com.wit.iot.order.service.OrderService;
import com.wit.iot.store.domain.Store;
import com.wit.iot.store.service.StoreService;
import com.wit.iot.user.domain.User;
import com.wit.iot.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    @Autowired
    private HandlerSubmit handlerSubmit;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ItemService itemService;

    @Test
    public void queryUsers(){
        User user = userService.getById(452044660240551959L);
        System.out.println(user.getCreateTime());
    }

    @Test
    public void queryStore(){
        Store user = storeService.getById(452044660240554938L);
        System.out.println(user.getCreateTime());
    }

    @Test
    public void queryItem(){
        //一共36万数据，分页70，随借取
        Integer pageNo = new Random().nextInt(69) + 1;
        List<Item> items = itemService.getListByPages(pageNo * 5000, 5000);

        System.out.println(items);
    }

    @Test
    public void insertOrder(){
        Order order = new Order(1l, 23l, 1l, 24l, new Date(), 1l, new Date(), 1l, 0l);
        orderService.save(order);
    }

    @Test
    public void insertOrderItem(){
        OrderItem order = new OrderItem(1l, 23l, 1l, 24l, 0);
        orderItemService.save(order);
    }

    @Test
    public void batchInsert(){
        handlerSubmit.handleOrderBatchInsert(null, null, 30);
    }
}
