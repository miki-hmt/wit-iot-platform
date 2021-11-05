package com.witsoft.gongli;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
@MapperScan({"com.witsoft.gongli.**.dao"}) //与dao层的@Mapper二选一写上即可(主要作用是扫包)
public class IotGongliServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotGongliServerApplication.class, args);
		log.info(">>>>>>>>>>IOT-SP服务项目启动<<<<<<<<<<");
	}

}
