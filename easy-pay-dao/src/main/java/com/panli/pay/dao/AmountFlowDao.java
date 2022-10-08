package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.AmountFlowPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface AmountFlowDao {
    int insert(@Param("po") AmountFlowPo po);

    AmountFlowPo queryOrderFlow(@Param("tradeId") String tradeId, @Param("channelTradeId") String channelTradeId);
}
