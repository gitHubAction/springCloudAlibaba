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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@EnableAutoConfiguration
@MapperScan(basePackages = {"com.seven.mutlidatasource.dao.pn"}, sqlSessionTemplateRef = "pnSqlSessionTemplate")
public class PrivacyNumberDataSourceConfig {


    /**
     * 根据配置参数创建数据源。使用派生的子类。
     *
     * @return 数据源
     */
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.privacynumber-uat")
    public DataSource pnDataSource() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        druidDataSource.setConnectionProperties("druid.pool.connectionProperties:druid.stat.mergeSql=true;druid.stat.slowSqlMillis=1000");
        return druidDataSource;
    }

    @Bean
    public DataSourceTransactionManager pnTransactionManager(@Qualifier("pnDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Autowired
    PageInterceptor pageHelper;

    @Bean
    public org.apache.ibatis.session.Configuration pnMybatisConfiguration(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(StdOutImpl.class);
        configuration.addInterceptor(pageHelper);
        return configuration;
    }


    @Bean
    public SqlSessionFactory pnSqlSessionFactory(@Qualifier("pnDataSource") DataSource dataSource,
                                                 @Qualifier("pnMybatisConfiguration") org.apache.ibatis.session.Configuration config) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(config);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/pn/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate pnSqlSessionTemplate(@Qualifier("pnSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
