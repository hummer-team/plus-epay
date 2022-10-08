package com.panli.pay.service.domain.payment.yug.context;

import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

@Data
public class YugBeanPaymentContext extends BasePaymentChannelReqBodyContext {
    private Integer beanNumber;
    @Override
    public String requestBody() {
        return "";
    }
}
