package com.wit.iot.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.iot.order.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BsOrderItemDao extends BaseMapper<OrderItem> {
}
