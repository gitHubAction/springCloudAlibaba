package com.seven.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/1 10:18
 */
@Slf4j
@RestController
public class AuthServerController {

    @GetMapping("/auth/refreshToken")
    public String refreshToken(@RequestParam String refreshToken){
        log.info("refreshToken ：{}", refreshToken);
        JSONObject result = new JSONObject();
        result.put("token", UUID.randomUUID().toString().toLowerCase());
        result.put("refresh_token",UUID.randomUUID().toString().toLowerCase());
        result.put("user","userName");
        return result.toJSONString();
    }


    @GetMapping("/auth/checkToken")
    public String checkToken(@RequestParam String token){
        log.info("checkToken ：{}", token);
        Random random = new Random();
        int i = random.nextInt(10);
        if(i > 5)return null;
        JSONObject result = new JSONObject();
        result.put("user","userName:"+token);
        log.info("res:{}", result);
        return result.toJSONString();
    }
}
