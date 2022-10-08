package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

@Data
public class WxAdvancePaymentReqContext extends BasePaymentChannelReqBodyContext {
    private WxAdvancePaymentReq body;

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
