package com.wit.iot.order.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class ThreadPoolConfig {

    @Bean("orderExecutors")
    public ExecutorService threadPool(){
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("wit-orders-pool-%d").build();
        return Executors.newCachedThreadPool(factory);
    }
}
