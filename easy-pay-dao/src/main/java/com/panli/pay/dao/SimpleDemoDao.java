package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.SimpleDemoPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface SimpleDemoDao {
    int insert(@Param("po") SimpleDemoPo po);
    SimpleDemoPo querySingleById(@Param("id")String id);
}