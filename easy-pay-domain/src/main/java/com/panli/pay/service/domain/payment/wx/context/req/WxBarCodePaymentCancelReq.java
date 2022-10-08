package com.panli.pay.service.domain.payment.wx.context.req;

import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class WxBarCodePaymentCancelReq extends BasePaymentChannelReqBodyContext {
    private String xmlBody;
    private String certPath;
    private String certPassword;
    private String certKsType;
    private String certName;
    private int retry;
    private String merchantId;
    private Map<String, Object> parameter = new TreeMap<>();

    @Override
    public String requestBody() {
        return xmlBody;
    }
}
