package com.seven.mutlidatasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/12 10:02
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MutliDatasourceAppliaction {

    public static void main(String[] args) {
        SpringApplication.run(MutliDatasourceAppliaction.class, args);
    }
}
