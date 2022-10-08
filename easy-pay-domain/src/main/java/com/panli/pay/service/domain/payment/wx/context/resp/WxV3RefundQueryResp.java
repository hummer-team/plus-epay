package com.panli.pay.service.domain.payment.wx.context.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class WxV3RefundQueryResp {
    @JSONField(name = "refund_id")
    private String refundId;
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    private String channel;
    @JSONField(name = "user_received_account")
    private String userReceivedAccount;

    @JSONField(name = "success_time",format = "yyyy-MM-dd'T'HH:mm:sssXXX")
    private Date successTime;
    @JSONField(name = "create_time",format = "yyyy-MM-dd'T'HH:mm:sssXXX")
    private Date createTime;

    private String status;
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
