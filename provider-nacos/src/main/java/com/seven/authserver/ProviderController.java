package com.seven.authserver;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/1 10:18
 */
@Slf4j
@RestController
public class ProviderController {

    @SneakyThrows
    @PostMapping(value = "/body")
    public String getBody(@RequestBody Map<String,String> body, HttpServletRequest request) {
        String user = URLDecoder.decode(request.getHeader("user"),"UTF-8");
        return "Hello "+ user +"！！！ Discovery  key:" + body.get("key") +" value:" + body.get("value");
    }

    @GetMapping(value = "/echo")
    public String echo() {
        return "Hello echo!!!";
    }
}
