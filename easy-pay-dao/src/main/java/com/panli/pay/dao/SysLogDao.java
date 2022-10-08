package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.PayCallRecordPo;
import com.panli.pay.support.model.po.SysLogPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface SysLogDao {
    int insert(@Param("po") SysLogPo po);
}
