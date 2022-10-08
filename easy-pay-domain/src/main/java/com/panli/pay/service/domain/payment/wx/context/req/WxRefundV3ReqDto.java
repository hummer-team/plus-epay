package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;

@Data
public class WxRefundV3ReqDto extends BasePaymentChannelReqBodyContext {
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "out_refund_no")
    private String outRefundNo;

    private String reason;
    private String notifyUrl;
    private Amount amount;

    @Data
    public static class Amount {
        private int refund;
        private int total;
        private String currency;
    }

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
