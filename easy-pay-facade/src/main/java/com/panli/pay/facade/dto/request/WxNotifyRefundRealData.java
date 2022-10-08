package com.panli.pay.facade.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class WxNotifyRefundRealData {
    private String mchid;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    @JSONField(name = "refund_id")
    private String refundId;
    @JSONField(name = "refund_status")
    private String refundStatus;

    @JSONField(name = "success_time",format = "yyyy-MM-dd'T'HH:mm:sssXXX")
    private Date successTime;

    private Amount amount;

    @Data
    public static class Amount {
        @JSONField(name = "payer_total")
        private Integer payerTotal;
        private Integer refund;
        private Integer total;
        private String currency;
        @JSONField(name = "payer_currency")
        private String payerCurrency;
        @JSONField(name = "payer_refund")
        private Integer payerRefund;
    }
}
