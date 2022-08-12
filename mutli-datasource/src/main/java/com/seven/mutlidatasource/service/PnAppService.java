package com.seven.mutlidatasource.service;

import com.seven.mutlidatasource.entity.PnApp;

import java.util.List;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @Date 2022/8/12 10:53
 */
public interface PnAppService {
    List<PnApp> selectAll();
}
