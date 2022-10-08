package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.NotifyRecordPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface NotifyRecordDao {
    int insert(@Param("po") NotifyRecordPo po);
}
