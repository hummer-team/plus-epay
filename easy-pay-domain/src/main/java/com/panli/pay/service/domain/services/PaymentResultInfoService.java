package com.panli.pay.service.domain.services;

import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.result.RefundResultContext;

public interface PaymentResultInfoService {
    void savePaymentResult(PaymentResultContext resultContext
            , PaymentContext paymentContext
            , String reqBody
            , boolean isBeanNumberPay
            , boolean createOrder
            , boolean createPayFlow);

    void saveRefundResult(RefundResultContext context, RefundContext refundContext, String reqBody);

    void saveRefundResultOfYugBean(RefundResultContext context, RefundContext refundContext);

    void saveProfitSharingResult(PaymentResultContext resultContext
            , PaymentContext paymentContext
            , String reqBody);
}
