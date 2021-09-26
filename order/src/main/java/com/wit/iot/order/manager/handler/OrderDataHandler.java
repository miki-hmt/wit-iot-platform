package com.wit.iot.order.manager.handler;

import com.wit.iot.item.domain.Item;
import com.wit.iot.order.domain.Order;
import com.wit.iot.order.domain.OrderItem;
import com.wit.iot.order.service.OrderItemService;
import com.wit.iot.order.service.OrderService;
import com.wit.iot.order.utils.FoundationContext;
import com.wit.iot.store.domain.Store;
import com.wit.iot.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class OrderDataHandler implements Callable<HandleResult> {

    private List<User> users;
    private List<Store> stores;

    private List<Item> items;
    private OrderService orderService;
    //setter注入，解决开闭原则问题
    private OrderItemService orderItemService;

    public OrderDataHandler(List<User> users, List<Store> stores, OrderService orderService) {
        this.users = users;
        this.stores = stores;
        this.orderService = orderService;
    }

    @Override
    public HandleResult call() {
        //打乱顺序,模拟真实下单场景
        Collections.shuffle(users);
        Collections.shuffle(stores);
        Collections.shuffle(items);

        //分布式id生成
        Long[] ids = FoundationContext.getLongKeyGenerator().next(users.size());
        Long[] orderItemIds = FoundationContext.getLongKeyGenerator().next(users.size());
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            User user = users.get(i);
            Item item = items.get(i);
            Order order = new Order(ids[i], store.getCreateUserId(), store.getStoreId(), user.getUserId(), new Date(), 1l, new Date(), 1l, 0l);
            OrderItem orderItem = new OrderItem(orderItemIds[i], order.getOrderId(), item.getItemId(), 1l, 1);

            orders.add(order);
            orderItems.add(orderItem);
        }

        orderService.saveBatch(orders);
        orderItemService.saveBatch(orderItems);
        return new HandleResult(stores.size(), Thread.currentThread().getName());
    }


    public void setOrderItemService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
