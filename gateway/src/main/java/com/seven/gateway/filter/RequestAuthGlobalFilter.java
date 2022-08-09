package com.seven.gateway.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.seven.gateway.feign.client.AuthFluxFeignClient;
import com.seven.gateway.feign.client.AuthHttpFeignClient;
import feign.form.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;

/**
 * @author
 * @version 1.0
 * @description
 * @date 2022/8/8 11:20
 */
@Slf4j
@Component
public class RequestAuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthFluxFeignClient authFluxFeignClient;

    @Autowired
    private AuthHttpFeignClient authHttpFeignClient;

    @Autowired
    private ExecutorService executorService;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = exchange.getRequest().getURI().getPath();
        if(path.startsWith("/webflux") || path.startsWith("/authServer")){
            return chain.filter(exchange);
        }
        final String token = getToken(request, "token");
        final String refresh_token = getToken(request, "refresh_token");
        if(StringUtils.isBlank(token) || StringUtils.isBlank(refresh_token)){
            // 没有token直接返回错误信息
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 同步转异步调用
        return
            // 校验token
                Mono
                    .fromCallable(()->
                            {
                                log.info("thread:{}     token:{}",Thread.currentThread().getName(), token);
                                // 校验token是否过期
                                return authHttpFeignClient.checkToken(token);
                            }
                    )
                    // 使用指定线程  避免使用gateway线程不支持同步阻塞调用
                    .publishOn(Schedulers.fromExecutorService(executorService))
                    // token过期则拿refresh_token刷新
                    .switchIfEmpty(Mono.fromCallable(()->{
                        log.info("switchIfEmpty   thread:{}     refreshToken:{}",Thread.currentThread().getName(), refresh_token);
                        return authHttpFeignClient.refreshToken(refresh_token);
                    }))
                    // 不管是刷新token还是校验token成功
                    .flatMap(res->{
                        log.info("flatMap   thread:{}     res:{}",Thread.currentThread().getName(), res);
                        if (StringUtils.isBlank(res)) {
                            // 没有token直接返回错误信息
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }else{
                            JSONObject resJson = JSONObject.parseObject(res);
                            JSONObject data = resJson.getJSONObject("data");
                            // 重塑请求
                            ServerHttpRequest mutateReq = null;
                            try {
                                mutateReq = exchange.getRequest()
                                        .mutate()
                                        // 刷新后的token
                                        .header("token", data.getString("token"))
                                        .header("refresh_token", data.getString("refresh_token"))
                                        // 用户信息
                                        .header("user", URLEncoder.encode("用户信息" + data.getString("user"), CharsetUtil.UTF_8.name()))
                                        .build();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return chain.filter(exchange.mutate().request(mutateReq).build());
                        }
                    });
//        调用webflux接口
//        return Mono.defer(()->
//            authClient
//                    .checkToken(token)
//                    .switchIfEmpty(authClient.refreshToken(refresh_token))
//                    .flatMap(checkRes -> {
//                        if (StringUtils.isBlank(checkRes)) {
//                            // 没有token直接返回错误信息
//                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                            return exchange.getResponse().setComplete();
//                        } else {
//                            JSONObject tokenRes = JSONObject.parseObject(checkRes);
//                            JSONObject data = tokenRes.getJSONObject("data");
//                            // 重塑请求
//                            ServerHttpRequest mutateReq = null;
//                            try {
//                                mutateReq = exchange.getRequest()
//                                        .mutate()
//                                        // 刷新后的token
//                                        .header("token", data.getString("token"))
//                                        .header("refresh_token", data.getString("refresh_token"))
//                                        // 用户信息
//                                        .header("user", URLEncoder.encode("用户信息" + data.getString("user"),CharsetUtil.UTF_8.name()))
//                                        .build();
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            return chain.filter(exchange.mutate().request(mutateReq).build());
//                        }
//                    })
//        ).subscribeOn(Schedulers.immediate());
    }

    private String getToken(ServerHttpRequest request, String key) {
        // 先从header中获取
        String token = request.getHeaders().getFirst(key);
        if(StringUtils.isNotBlank(token)){
            return token;
        }
        // 从cookie中取
        HttpCookie cookie = request.getCookies().getFirst(key);
        if(cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            return cookie.getValue();
        }
        // 从参数中取
        return request.getQueryParams().getFirst(key);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
