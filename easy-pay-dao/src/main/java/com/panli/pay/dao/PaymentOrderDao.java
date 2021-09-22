/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

    PaymentOrderPo queryOneByTradeIdAndCode(@Param("tradeId") String tradeId, @Param("channelCode") String channelCode);

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

    int updatePaymentStatus(@Param("tradeId") String tradeId, @Param("requestId")
            String requestId, @Param("status") int status, @Param("channelTradeId") String channelTradeId
            , @Param("channelTradeStatus") String channelTradeStatus);

    int updateRefundStatus(@Param("tradeId") String tradeId
            , @Param("status") int status
            , @Param("channelTradeId") String channelTradeId
            , @Param("channelRefundId") String channelRefundId
            , @Param("channelRefundDateTime") Date channelRefundDateTime
            , @Param("channelRefundStatus") String channelRefundStatus);

    PaymentOrderPo queryOutChannelOrder(@Param("channelTradeId") String channelTradeId);

    PaymentOrderPo queryByTradeIdAndExcludeYugBean(@Param("tradeId") String tradeId, @Param("userId") String userId);
}
