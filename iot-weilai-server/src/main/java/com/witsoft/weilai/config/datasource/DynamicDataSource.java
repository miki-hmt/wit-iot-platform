package com.witsoft.weilai.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 动态数据源
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {


    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }


    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
