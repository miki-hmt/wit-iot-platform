package com.wit.iot.order.manager.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wit.iot.item.domain.Item;
import com.wit.iot.item.service.ItemService;
import com.wit.iot.order.service.OrderItemService;
import com.wit.iot.order.service.OrderService;
import com.wit.iot.store.domain.Store;
import com.wit.iot.store.service.StoreService;
import com.wit.iot.user.domain.User;
import com.wit.iot.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j
@Component
public class HandlerSubmit {

    @Autowired
    @Qualifier("orderExecutors")
    private ExecutorService executorService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ItemService itemService;


    public Boolean handleOrderBatchInsert(Integer pageNo, Integer pageSize){

        return handleOrderBatchInsert(pageNo, pageSize, 30);
    }


    public Boolean handleOrderBatchInsert(Integer pageNo, Integer pageSize, Integer threadSize){

        CompletionService<HandleResult> completionService = new ExecutorCompletionService<>(executorService);

        //用户2000个
        if(pageNo == null)
            pageNo = 9;

        if(pageSize == null)
            pageSize = 5000;

        //Page<User> page = new Page<>(userPageNo, userPageSize);
        com.github.pagehelper.Page<User> pageObject = PageHelper.startPage(pageNo, pageSize);
        List<User> users = userService.list();

        //Page<Store> storePage = new Page<>(userPageNo, userPageSize);
        com.github.pagehelper.Page<User> storePage = PageHelper.startPage(pageNo, pageSize);
        List<Store> stores = storeService.list();

        for (int i = 0; i < threadSize; i++) {
            //一共36万数据，分页70，随借取
            Integer pageRandomNo = new Random().nextInt(69) + 1;
            List<Item> items = itemService.getListByPages(pageRandomNo * pageSize, pageSize);
            log.info("商品分页信息：{}, 数量：{}", pageNo, items.size());
            OrderDataHandler orderDataHandler = new OrderDataHandler(users, stores, orderService);
            orderDataHandler.setOrderItemService(orderItemService);
            orderDataHandler.setItems(items);

            completionService.submit(orderDataHandler);
        }

        //读取结果
        for (int i = 0; i < threadSize; i++) {
            try {
                HandleResult result = completionService.take().get(30, TimeUnit.MINUTES);
                log.info("线程：{}执行结束，数据处理量：{}", result.getThreadName(), result.getCount());
            } catch (InterruptedException e) {
                log.error("线程异常终止...{}", e.getMessage());
            } catch (ExecutionException e) {
                log.error("线程执行异常...{}", e.getMessage());
            } catch (TimeoutException e) {
                log.error("线程超时异常...{}", e.getMessage());
            }
        }

        return true;
    }
}
