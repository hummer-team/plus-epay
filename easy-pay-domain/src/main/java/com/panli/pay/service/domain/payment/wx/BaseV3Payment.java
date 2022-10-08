package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.service.domain.enums.PaymentStatusEnum;

import java.util.Map;

public abstract class BaseV3Payment extends BaseWxPayment {

    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {

        return null;
    }

    @Override
    public boolean successOfOption(Map<String, Object> resultMap) {

        return true;
    }
}
