package com.seven.mutlidatasource.service.impl;

import com.seven.mutlidatasource.dao.pn.PnAppMapper;
import com.seven.mutlidatasource.entity.PnApp;
import com.seven.mutlidatasource.service.PnAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/8/12 10:54
 */
@Service
public class PnAppServiceImpl implements PnAppService {

    @Autowired
    private PnAppMapper pnAppMapper;

    @Override
    public List<PnApp> selectAll() {
        return pnAppMapper.selectAll(new PnApp());
    }
}
