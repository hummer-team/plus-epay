package com.panli.pay.service.domain.payment.yug.context;

import com.alibaba.fastjson.JSON;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class YugBeanRefundContext extends BasePaymentChannelReqBodyContext {
    private BigDecimal beanNumber;
    private String tradeId;
    private String channelTradeId;
    private String refundBatchId;

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
