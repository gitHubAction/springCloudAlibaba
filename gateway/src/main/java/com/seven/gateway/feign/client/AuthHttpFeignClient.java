package com.seven.gateway.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/3 12:38
 */
// 通过网关服务调用鉴权服务webflux
@FeignClient(name = "authServer", url = "http://localhost:8081/authServer")
// 通过nacos服务直连
//@FeignClient(name = "authServer")
public interface AuthHttpFeignClient {

    @GetMapping(value = "/auth/refreshToken")
    String refreshToken(@RequestParam(name = "refreshToken") String refreshToken);


    @GetMapping(value = "/auth/checkToken")
    String checkToken(@RequestParam(name = "token") String token);
}
