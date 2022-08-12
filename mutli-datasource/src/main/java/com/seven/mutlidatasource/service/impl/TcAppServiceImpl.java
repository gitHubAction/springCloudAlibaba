package com.seven.mutlidatasource.service.impl;

import com.seven.mutlidatasource.dao.tc.TcAppMapper;
import com.seven.mutlidatasource.entity.TcApp;
import com.seven.mutlidatasource.service.TcAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/12 10:55
 */
@Service
public class TcAppServiceImpl implements TcAppService {

    @Autowired
    private TcAppMapper tcAppMapper;

    @Override
    public List<TcApp> selectAll() {
        return tcAppMapper.selectAll(new TcApp());
    }
}
