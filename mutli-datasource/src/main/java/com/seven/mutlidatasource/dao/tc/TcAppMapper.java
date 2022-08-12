package com.seven.mutlidatasource.dao.tc;

import com.seven.mutlidatasource.dao.base.MyBatisBaseDao;
import com.seven.mutlidatasource.entity.TcApp;
import org.apache.ibatis.annotations.Mapper;

/**
 * TcAppMapper继承基类
 */
@Mapper
public interface TcAppMapper extends MyBatisBaseDao<TcApp, Long> {
}