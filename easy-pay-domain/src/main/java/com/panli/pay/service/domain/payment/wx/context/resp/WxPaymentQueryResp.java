package com.panli.pay.service.domain.payment.wx.context.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxPaymentQueryResp {
    @JSONField(name = "appid")
    private String appId;
    @JSONField(name = "mchid")
    private String mchId;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "trade_type")
    private String tradeType;
    @JSONField(name = "trade_state")
    private String tradeState;
    @JSONField(name = "trade_state_desc")
    private String tradeStateDesc;
    @JSONField(name = "bank_type")
    private String bankType;
    @JSONField(name = "attach")
    private String attach;
    @JSONField(name = "success_time")
    private String successTime;
    private Payer payer;
    private Amount amount;

    @Data
    public static class Amount {
        private int total;
        @JSONField(name = "payer_total")
        private int payerTotal;
        private String currency;
    }

    @Data
    public static class Payer {
        @JSONField(name = "openid")
        private String openId;
    }
}
