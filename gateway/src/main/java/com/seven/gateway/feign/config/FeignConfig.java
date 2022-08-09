package com.seven.gateway.feign.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/3 12:43
 */
@Configuration
public class FeignConfig {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    @Bean(name = "executorService")
    public ExecutorService executorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 可用CPU数量
        int core = Runtime.getRuntime().availableProcessors();
        // 核心线程数
        executor.setCorePoolSize(core);
        // 最大线程数
        executor.setMaxPoolSize(core * 2 + 1);
        // 核心线程和最大线程之间的线程保持存活的时间
        executor.setKeepAliveSeconds(3);
        // 阻塞队列大小
        executor.setQueueCapacity(40);
        // 线程池的名称前缀
        executor.setThreadNamePrefix("thread-pool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 后端守护线程
        executor.setDaemon(true);
        // 执行初始化
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}