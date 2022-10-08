package com.panli.pay.service.domain.payment.ali.context;

import com.alipay.api.AlipayClient;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

/**
 * @author edz
 */
@Data
public class AliPaymentChannelReqContext<T> extends BasePaymentChannelReqBodyContext {
    private AlipayClient alipayClient;
    private transient T aliPayRequest;
    private String requestBody;

    @Override
    public String requestBody() {
        return requestBody;
    }
}
