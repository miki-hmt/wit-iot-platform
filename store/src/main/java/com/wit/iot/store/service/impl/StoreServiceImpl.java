package com.wit.iot.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.iot.store.domain.Store;
import com.wit.iot.store.mapper.StoreMapper;
import com.wit.iot.store.service.StoreService;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
}
