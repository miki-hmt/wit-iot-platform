package com.wit.iot.third;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName IotThirdServerApplation
 * @Description TODO
 * @Author miki
 * @Date 2021/6/20 16:12
 * @Version 1.0
 */

//指定扫描根路径，否则子模块配置不会被扫描
@ComponentScan(basePackages = {"com.wit.iot"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class IotThirdServerApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(IotThirdServerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
