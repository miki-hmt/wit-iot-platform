package com.wit.iot.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.iot.item.domain.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    public List<Item> selectItemPage(@Param("offset") Integer offset, @Param("count")Integer count);
}
