/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
