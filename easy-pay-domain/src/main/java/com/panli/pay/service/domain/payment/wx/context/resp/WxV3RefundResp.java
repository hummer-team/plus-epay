package com.panli.pay.service.domain.payment.wx.context.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxV3RefundResp {
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
    @JSONField(name = "create_time")
    private String createTime;
    private String status;

    @Data
    public static class Amount {
        private int total;
        private int refund;
        @JSONField(name = "payer_total")
        private int payerTotal;
        @JSONField(name = "payer_refund")
        private int payerRefund;
        @JSONField(name = "settlement_refund")
        private int settlementRefund;
        @JSONField(name = "settlement_total")
        private int settlementTotal;
        @JSONField(name = "discount_refund")
        private int discountRefund;
        private String currency;
    }
}
