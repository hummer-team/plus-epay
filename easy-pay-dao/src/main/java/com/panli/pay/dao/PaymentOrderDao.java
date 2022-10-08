package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@DaoAnnotation
public interface PaymentOrderDao {
    int insertPayOrder(@Param("po") PaymentOrderPo po);

    int insertRefundOrder(@Param("po") PaymentOrderPo po);

    List<PaymentOrderPo> queryByTradeId(@Param("tradeId") String tradeId);

    List<PaymentOrderPo> queryByTradeIdAndCode(@Param("tradeId") String tradeId, @Param("platformCode") String platformCode);

    PaymentOrderPo queryOneByTradeIdAndCode(@Param("platformCode") String platformCode, @Param("tradeId") String tradeId);

    PaymentOrderPo queryOneByTradeIdAndCodeAndUserId(@Param("tradeId") String tradeId
            , @Param("channelCode") String channelCode
            , @Param("userId") String userId);

    List<PaymentOrderPo> queryByTradeIdChannelCodeUserId(@Param("tradeId") String tradeId
            , @Param("channelCode") String channelCode
            , @Param("userId") String userId);

    PaymentOrderPo queryByTradeIdByCode(@Param("tradeId") String tradeId, @Param("platformCode") String platformCode
            , @Param("channelCode") String channelCode);

    boolean queryExistsRefundOrder(@Param("tradeId") String tradeId
            , @Param("channelCode") String channelCode
            , @Param("userId") String userId
            , @Param("batchId") String batchId);

    int updatePaymentStatus(PaymentOrderPo po);

    int updateRefundStatus(@Param("tradeId") String tradeId
            , @Param("status") int status
            , @Param("channelTradeId") String channelTradeId
            , @Param("channelRefundId") String channelRefundId
            , @Param("channelRefundDateTime") Date channelRefundDateTime
            , @Param("channelRefundStatus") String channelRefundStatus);

    PaymentOrderPo queryOutChannelOrder(@Param("channelTradeId") String channelTradeId);

    PaymentOrderPo queryByTradeIdAndExcludeYugBean(@Param("tradeId") String tradeId, @Param("userId") String userId);
}
