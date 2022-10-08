package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

@Data
public class WxV3RefundQueryReq extends BasePaymentChannelReqBodyContext {
    @JSONField(name = "mchid")
    private String mchId;
    @JSONField(name = "appid")
    private String appId;


    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
