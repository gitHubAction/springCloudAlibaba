package com.seven.mutlidatasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.seven.mutlidatasource.dao.tc"}, sqlSessionTemplateRef = "tcSqlSessionTemplate")
public class TinyConfigDataSourceConfig {


    /**
     * 根据配置参数创建数据源。使用派生的子类。
     *
     * @return 数据源
     */
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.tiny-config")
    @Primary
    public DataSource tcDataSource() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        druidDataSource.setConnectionProperties("druid.pool.connectionProperties:druid.stat.mergeSql=true;druid.stat.slowSqlMillis=1000");
        return druidDataSource;
    }

    @Bean
    public DataSourceTransactionManager tcTransactionManager(@Qualifier("tcDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Resource
    PageInterceptor interceptor;


    @Bean
    public org.apache.ibatis.session.Configuration tcMybatisConfiguration(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(StdOutImpl.class);
//        Interceptor interceptor = new PageInterceptor();
//        Properties properties = new Properties();
//        properties.setProperty("helperDialect", "mysql");
//        properties.setProperty("reasonable", "true");
//        properties.setProperty("supportMethodsArguments","true");
//        properties.setProperty("params","count=countSql");
//        interceptor.setProperties(properties);
        configuration.addInterceptor(interceptor);
        return configuration;
    }

    @Bean
    public SqlSessionFactory tcSqlSessionFactory(@Qualifier("tcDataSource") DataSource dataSource,
                                                  @Qualifier("tcMybatisConfiguration") org.apache.ibatis.session.Configuration config) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(config);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/tc/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate tcSqlSessionTemplate(@Qualifier("tcSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
