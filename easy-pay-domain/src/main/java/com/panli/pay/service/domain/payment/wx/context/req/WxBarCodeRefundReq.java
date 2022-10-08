package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

@Data
public class WxBarCodeRefundReq extends BasePaymentChannelReqBodyContext {

    private String xmlBody;
    private String certPath;
    private String certPassword;
    private String certKsType;
    private String certName;
    private int retry;
    private String merchantId;

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
