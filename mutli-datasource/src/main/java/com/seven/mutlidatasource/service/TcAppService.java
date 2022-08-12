package com.seven.mutlidatasource.service;

import com.seven.mutlidatasource.entity.TcApp;

import java.util.List;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @Date 2022/8/12 10:54
 */
public interface TcAppService {
    List<TcApp> selectAll();
}
