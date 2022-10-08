package com.panli.pay.service.domain.payment.yug.context;

import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

@Data
public class YugBeanPaymentQueryContext extends BasePaymentChannelReqBodyContext {
    private String tradeId;
    private String channelCode;
    private String channelTradeId;
    @Override
    public String requestBody() {
        return "";
    }
}
