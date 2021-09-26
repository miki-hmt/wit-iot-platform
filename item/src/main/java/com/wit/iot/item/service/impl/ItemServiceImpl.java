package com.wit.iot.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.iot.item.domain.Item;
import com.wit.iot.item.mapper.ItemMapper;
import com.wit.iot.item.service.ItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Override
    public List<Item> getListByPages(Integer offset, Integer count) {
        return itemMapper.selectItemPage(offset, count);
    }
}
