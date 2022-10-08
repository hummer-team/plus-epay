package com.panli.pay.facade.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class WxNotifyPaymentRealData {
    @JSONField(name = "transaction_id")
    private String transactionId;
    private String appid;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    private Amount amount;
    private String mchid;
    @JSONField(name = "trade_state")
    private String tradeState;
    @JSONField(name = "trade_state_desc")
    private String tradeStateDesc;
    @JSONField(name = "bank_type")
    private String bankType;
    private Payer payer;
    @JSONField(name = "attach")
    private String attach;
    @JSONField(name = "success_time",format = "yyyy-MM-dd'T'HH:mm:sssXXX")
    private Date successTime;
    @Data
    public static class Amount {
        @JSONField(name = "payer_total")
        private Integer payerTotal;
        private Integer total;
        private String currency;
        @JSONField(name = "payer_currency")
        private String payerCurrency;
    }

    @Data
    public static class Payer {
        private String openid;
    }
}

