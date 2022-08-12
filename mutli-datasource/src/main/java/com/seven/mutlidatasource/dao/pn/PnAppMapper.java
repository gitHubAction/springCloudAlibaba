package com.seven.mutlidatasource.dao.pn;

import com.seven.mutlidatasource.dao.base.MyBatisBaseDao;
import com.seven.mutlidatasource.entity.PnApp;
import org.apache.ibatis.annotations.Mapper;

/**
 * PmAppMapper继承基类
 *
 * @author gushiye
 */
@Mapper
public interface PnAppMapper extends MyBatisBaseDao<PnApp,Long> {
}