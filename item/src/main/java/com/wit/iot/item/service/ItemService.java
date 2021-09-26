package com.wit.iot.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.iot.item.domain.Item;

import java.util.List;

public interface ItemService extends IService<Item> {

    public List<Item> getListByPages(Integer offset, Integer count);
}
