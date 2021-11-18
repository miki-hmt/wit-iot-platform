package com.witsoft.weilai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 定时造数场景
 * @author miki
 * @desc 不加@EnableScheduling注解，定时器无效！！！
 */
@EnableScheduling
@Component
@Slf4j
public class ScheduleConfig {
    private final static Boolean flag = false;

    //每2小时造一批数据
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void mockTemperatureData(){
        try{
            log.info("### 开始生产当前2小时的已处理设备数据...");

            log.info("### 已处理设备数据新增成功...");
        }catch (Exception e){
            log.error("批量插入温度数据异常，请检查：{}", e.getMessage());
        }
    }
}
