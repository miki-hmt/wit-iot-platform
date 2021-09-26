package com.wit.iot.third.controller;

import com.wit.iot.common.core.result.AjaxResult;
import com.wit.iot.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/insert")
    public AjaxResult batchInsert(@RequestParam Integer pageNo, @RequestParam Integer pageSize){

        Boolean aBoolean = orderService.handleOrderBatchInsert(pageNo, pageSize);
        return AjaxResult.success(aBoolean);
    }
}
