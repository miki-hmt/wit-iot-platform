package com.wit.iot.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.iot.order.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BsOrderDao extends BaseMapper<Order> {
}
