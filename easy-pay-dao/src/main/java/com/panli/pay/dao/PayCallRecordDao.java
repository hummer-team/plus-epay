package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.PayCallRecordPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface PayCallRecordDao {
    int insert(@Param("po")PayCallRecordPo po);
}
