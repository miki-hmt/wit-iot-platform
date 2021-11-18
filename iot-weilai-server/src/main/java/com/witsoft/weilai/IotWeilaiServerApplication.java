package com.witsoft.weilai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.witsoft.weilai.dao")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class IotWeilaiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotWeilaiServerApplication.class, args);
	}

}
