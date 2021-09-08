package com.wit.iot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.iot.store.domain.Store;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper extends BaseMapper<Store> {
}
