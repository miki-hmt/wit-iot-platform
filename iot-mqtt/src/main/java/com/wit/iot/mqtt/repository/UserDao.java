package com.wit.iot.mqtt.repository;


import com.wit.iot.mqtt.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Slf4j
@Repository
public class UserDao {

    @Resource
    CassandraTemplate cassandraTemplate;

    public Boolean insert(User user){
        User insert = cassandraTemplate.insert(user);
        log.info("插入信息：{}", insert);
        return true;
    }
}
