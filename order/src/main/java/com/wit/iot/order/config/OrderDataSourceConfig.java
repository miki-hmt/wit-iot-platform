package com.wit.iot.order.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @ClassName DeviceDataSourceConfig
 * @Description TODO
 * @Author miki
 * @Date 2021/6/20 15:56
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableAutoConfiguration(exclude = {MybatisPlusAutoConfiguration.class})
@MapperScan(basePackages = OrderDataSourceConfig.USER_BASE_PACKAGE, sqlSessionFactoryRef = "orderSqlSessionFactory")
public class OrderDataSourceConfig {

    static final String USER_BASE_PACKAGE = "com.wit.iot.order.mapper";

    private static final String USER_MAPPER_LOCATION = "classpath*:mapper/order/*.xml";

    @Value("${spring.datasource.dynamic.order.url}")
    private String url;
    @Value("${spring.datasource.dynamic.order.username}")
    private String username;
    @Value("${spring.datasource.dynamic.order.password}")
    private String password;

    @Value("${spring.datasource.dynamic.order.driver-class-name}")
    private String driverClassName;

    @Bean(name = "orderDataSource")
    public DataSource orderDataSource() throws SQLException {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setPassword(password);
        dataSource.setUsername(username);

        //druid通用配置
        dataSource.setFilters("stat");
        //初始化时建立物理连接的个数
        dataSource.setInitialSize(1);
        //最大连接池数量
        dataSource.setMaxActive(20);
        //最小连接池数量
        dataSource.setMinIdle(1);
        //获取连接时最大等待时间，单位毫秒。
        dataSource.setMaxWait(60000);
        //间隔多久进行一次检测，检测需要关闭的空闲连接
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        //一个连接在池中最小生存的时间
        dataSource.setMinEvictableIdleTimeMillis(300000);
        //用来检测连接是否有效的sql
        dataSource.setValidationQuery("SELECT 'x'");
        //建议配置为true，不影响性能，并且保证安全性。
        dataSource.setTestWhileIdle(true);
        //申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        //是否缓存preparedStatement，也就是PSCache，oracle设为true，mysql设为false。分库分表较多推荐设置为false
        dataSource.setPoolPreparedStatements(false);
        // 打开PSCache时，指定每个连接上PSCache的大小
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        log.info("order 数据源初始化成功...");
        return dataSource;
    }

    @Bean(name = "orderTransactionManager")
    public DataSourceTransactionManager orderTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(orderDataSource());
    }

    @Bean(name = "orderSqlSessionFactory")
    @ConditionalOnMissingBean(name = "orderSqlSessionFactory")
    public SqlSessionFactory userSqlSessionFactory(
            @Qualifier("orderDataSource") DataSource orderDataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(orderDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(OrderDataSourceConfig.USER_MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
