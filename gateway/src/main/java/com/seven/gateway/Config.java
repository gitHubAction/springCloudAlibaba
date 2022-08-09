package com.seven.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/7/19 9:55
 */
@Configuration
public class Config {
    @Bean
    public RouteLocator myRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route(
                        p -> p
                                .path("/get/**")
                                .filters(f->f.addRequestHeader("hello","world"))
                                .uri("http://httpbin.org"))
                .build();
    }
}
