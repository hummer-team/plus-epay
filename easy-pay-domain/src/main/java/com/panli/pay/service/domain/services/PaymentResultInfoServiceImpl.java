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

package com.panli.pay.service.domain.services;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.core.PropertiesContainer;
import com.panli.pay.dao.PayCallRecordDao;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.context.RefundResultContext;
import com.panli.pay.service.domain.enums.OrderTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.support.model.po.PayCallRecordPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;

@Service
public class PaymentResultInfoServiceImpl implements PaymentResultInfoService {
    @Autowired
    private PaymentOrderDao paymentOrderDao;
    @Autowired
    private PayCallRecordDao recordDao;

    @Override
    public void saveProfitSharingResult(PaymentResultContext resultContext
            , PaymentContext paymentContext
            , String reqBody) {
        addPayFlowRecord(resultContext, paymentContext, reqBody);
    }

    public void addPayFlowRecord(PaymentResultContext resultContext, PaymentContext paymentContext, String reqBody) {
        PayCallRecordPo recordPo = ObjectCopyUtils.copy(resultContext, PayCallRecordPo.class);
        recordPo.setPlatformCode(paymentContext.getPlatformCode());
        recordPo.setChannelCode(paymentContext.getChannelCode());
        recordPo.setBusinessCode(paymentContext.getPlatformSubType());
        recordPo.setCallCostTimeMills(resultContext.getCostTimeMills());
        recordPo.setOriginRespBody(StringUtils.substring(resultContext.getChannelOriginResponse()
                , 0
                , PropertiesContainer.valueOfInteger("channel.origin.resp.max.length", (int) Short.MAX_VALUE)));
        recordPo.setRequestBody(reqBody);
        recordPo.setRequestId(MDC.get(REQUEST_ID));
        recordPo.setBatchId(paymentContext.getBatchId());
        recordPo.setTradeId(paymentContext.getTradeId());
        recordPo.setBatchId(paymentContext.getBatchId());

        recordDao.insert(recordPo);
    }

    // TODO: 2021/9/17 use hummer.framework @TargetDataSourceTM

    @Override
    public void savePaymentResult(PaymentResultContext resultContext
            , PaymentContext paymentContext
            , String reqBody
            , boolean isBeanNumberPay, boolean createOrder) {
        if (!isBeanNumberPay) {
            addPayFlowRecord(resultContext, paymentContext, reqBody);
        }

        if (createOrder) {
            PaymentOrderPo orderPo = new PaymentOrderPo();
            orderPo.setAmount(paymentContext.getAmount());
            //default 0
            orderPo.setPlatformSubType(paymentContext.getPlatformSubType());
            orderPo.setTradeId(paymentContext.getTradeId());
            orderPo.setPaymentUserId(paymentContext.getUserId());
            orderPo.setChannelTradeId(resultContext.getChannelTradeId());
            orderPo.setChannelAdvancePaymentId(resultContext.getChannelAdvancePaymentId());

            orderPo.setPayChannelType(paymentContext.getPayChannelType().getCode());
            orderPo.setPlatformCode(paymentContext.getPlatformCode());
            orderPo.setRequestId(MDC.get(REQUEST_ID));
            orderPo.setStatusCode(resultContext.getPaymentStatus().getCode());
            orderPo.setPaymentDateTime(resultContext.getPaymentDateTime());
            orderPo.setChannelCode(paymentContext.getChannelCode());
            orderPo.setOrderTag(paymentContext.getOrderTag());
            orderPo.setRefundBatchId(paymentContext.getBatchId());
            orderPo.setMerchantId(paymentContext.getMerchantId());
            orderPo.setOrderType(paymentContext.getOrderType().ordinal());
            paymentOrderDao.insertPayOrder(orderPo);
        }
    }

    @Override
    public void saveRefundResult(RefundResultContext context, RefundContext refundContext, String reqBody) {
        PayCallRecordPo recordPo = new PayCallRecordPo();
        recordPo.setPlatformCode(refundContext.getPlatformCode());
        recordPo.setChannelCode(refundContext.getChannelCode());
        recordPo.setBusinessCode(refundContext.getPlatformSubType());
        recordPo.setCallCostTimeMills(context.getCostTimeMills());
        recordPo.setOriginRespBody(StringUtils.substring(JSON.toJSONString(context.getChannelOriginResponse())
                , 0
                , PropertiesContainer.valueOfInteger("channel.origin.resp.max.length", (int) Short.MAX_VALUE)));
        recordPo.setRequestBody(reqBody);
        recordPo.setRequestId(MDC.get(REQUEST_ID));
        recordDao.insert(recordPo);


        PaymentOrderPo orderPo = new PaymentOrderPo();
        orderPo.setAmount(refundContext.getAmount());
        //default 0
        orderPo.setRefundBatchId(refundContext.getRefundBatchId());
        orderPo.setTradeId(refundContext.getTradeId());
        orderPo.setPaymentUserId(refundContext.getUserId());
        orderPo.setChannelTradeId(refundContext.getChannelTradeId());
        orderPo.setPlatformCode(refundContext.getPlatformCode());
        orderPo.setRequestId(MDC.get(REQUEST_ID));
        orderPo.setStatusCode(context.isSuccess()
                ? PaymentStatusEnum.REFUND_SUCCESS.getCode()
                : PaymentStatusEnum.REFUND_FAILED.getCode());
        orderPo.setChannelCode(refundContext.getChannelCode());
        //flowPo.setOrderTag(refundContext.get);
        paymentOrderDao.insertPayOrder(orderPo);
    }

    @Override
    public void saveRefundResultOfYugBean(RefundResultContext context, RefundContext refundContext) {
        PaymentOrderPo orderPo = refundContext.getPaymentOrder();
        assert orderPo != null;

        PaymentOrderPo refundOrderPo = new PaymentOrderPo();

        refundOrderPo.setRefundBatchId(refundContext.getRefundBatchId());
        refundOrderPo.setChannelCode(refundContext.getChannelCode());
        refundOrderPo.setTradeId(refundContext.getTradeId());
        refundOrderPo.setChannelTradeId(refundContext.getChannelTradeId());
        refundOrderPo.setAmount(refundContext.getAmount());
        refundOrderPo.setPlatformCode(refundContext.getPlatformCode());
        refundOrderPo.setChannelRefundId(context.getChannelRefundId());
        refundOrderPo.setPaymentUserId(refundContext.getUserId());
        refundOrderPo.setRequestId(MDC.get(REQUEST_ID));
        refundOrderPo.setStatusCode(context.getStatus().getCode());
        refundOrderPo.setOrderTag(orderPo.getOrderTag());
        refundOrderPo.setPlatformSubType(orderPo.getPlatformSubType());
        refundOrderPo.setChannelTradeStatus(orderPo.getChannelTradeStatus());
        refundOrderPo.setChannelRefundStatus(context.getChannelRefundStatus());
        refundOrderPo.setOrderType(OrderTypeEnum.REFUND_ORDER.ordinal());
        refundOrderPo.setPayType(orderPo.getPayType());
        refundOrderPo.setMerchantId(orderPo.getMerchantId());
        refundOrderPo.setPaymentDateTime(orderPo.getPaymentDateTime());

        paymentOrderDao.insertRefundOrder(refundOrderPo);
    }
}
