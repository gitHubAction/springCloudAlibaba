package com.seven.mutlidatasource.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.seven.mutlidatasource.entity.PnApp;
import com.seven.mutlidatasource.entity.TcApp;
import com.seven.mutlidatasource.service.PnAppService;
import com.seven.mutlidatasource.service.TcAppService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/12 10:50
 */
@RestController
public class FixedController {

    @Autowired
    private PnAppService pnAppService;

    @Autowired
    private TcAppService tcAppService;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @GetMapping("fixAll")
    public Object fixAll(){
        PageHelper.startPage(0,10);
        List<PnApp> pnAppList = pnAppService.selectAll();

        PageHelper.startPage(0,10);
        List<TcApp> tcAppList = tcAppService.selectAll();
        List<Object> res = new ArrayList();
        res.addAll(pnAppList);
        res.addAll(tcAppList);
        return JSON.toJSON(res);
    }

    @SneakyThrows
    @GetMapping("thread")
    public Object thread(){
        CompletableFuture<ArrayList<Object>> future = CompletableFuture.supplyAsync(() -> new ArrayList<Object>(pnAppService.selectAll()), executor);
        CompletableFuture<ArrayList<Object>> future1 = CompletableFuture.supplyAsync(() -> new ArrayList<Object>(tcAppService.selectAll()), executor);
        ArrayList<Object> objects = future.get();
        ArrayList<Object> objects2 = future1.get();
        objects.addAll(objects2);
        return JSON.toJSON(objects);
    }
}
