package com.witsoft.weilai.common.annotation;

import com.witsoft.weilai.enums.DataSourceType;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DB
{
    /**
     * 切换数据源名称
     */
     DataSourceType value() default DataSourceType.DB1;
}
