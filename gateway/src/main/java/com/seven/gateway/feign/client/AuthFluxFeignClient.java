package com.seven.gateway.feign.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/3 12:38
 */
// 通过网关服务调用鉴权服务webflux
//@ReactiveFeignClient(name = "webflux", url = "http://localhost:8081/webflux")
// 通过nacos服务直连
@ReactiveFeignClient(name = "webflux")
public interface AuthFluxFeignClient {

    @GetMapping(value = "/auth/refreshToken")
    Mono<String> refreshToken(@RequestParam(name = "refreshToken") String refreshToken);


    @GetMapping(value = "/auth/checkToken")
    Mono<String> checkToken(@RequestParam(name = "token") String token);
}
