package com.seven.webflux.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.UUID;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/8 11:42
 */
@Slf4j
@RestController
public class AuthController {


    @GetMapping("/auth/refreshToken")
    public Mono<String> refreshToken(@RequestParam String refreshToken){
        log.info("refreshToken ：{}", refreshToken);
        JSONObject result = new JSONObject();
        result.put("token", UUID.randomUUID().toString().toLowerCase());
        result.put("refresh_token",UUID.randomUUID().toString().toLowerCase());
        result.put("user","userName");
        return Mono.just(result.toJSONString());
    }


    @GetMapping("/auth/checkToken")
    public Mono<String> checkToken(@RequestParam String token){
        log.info("checkToken ：{}", token);
        Random random = new Random();
        int i = random.nextInt(10);
        if(i > 5)return Mono.empty();
        JSONObject result = new JSONObject();
        result.put("user","userName:"+token);
        log.info("res:{}", result);
        return Mono.just(result.toJSONString());
    }
}
