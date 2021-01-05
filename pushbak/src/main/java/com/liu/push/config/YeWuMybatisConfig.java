package com.liu.push.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"com.liu.push.liu.**.mapper"}, sqlSessionTemplateRef = "yeWuSqlSessionTemplate")
public class YeWuMybatisConfig {
    @Value("${yewu.mybatis.mapperLocations}")
    private String yewuMapper;

    /**
     * DataSource 自动配置并注册
     *
     * @return data source
     */
    @Bean("shi")
    @Primary
    @ConfigurationProperties(prefix = "datasource.shi")
    public DataSource shi() {
        return DruidDataSourceBuilder.create().build();
    }

   @Bean("binhai")
    @ConfigurationProperties(prefix = "datasource.binhai")
    public DataSource binhai() {
        return DruidDataSourceBuilder.create().build();
    }



    /**
     * 注册动态数据源
     *
     * @return
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        //添加数据源入口
        dataSourceMap.put("shi", shi());
        dataSourceMap.put("binhai", binhai());
        dynamicRoutingDataSource.setDefaultTargetDataSource(shi());// 设置默认数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        return dynamicRoutingDataSource;
    }


    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(yewuMapper));
        // 必须将动态数据源添加到 sqlSessionFactoryBean
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean;
    }


    @Bean
    @Primary
    public SqlSessionTemplate yeWuSqlSessionTemplate(@Qualifier("sqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        // 使用上面配置的Factory
        return template;
    }

    /**
     * 事务管理器
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

}