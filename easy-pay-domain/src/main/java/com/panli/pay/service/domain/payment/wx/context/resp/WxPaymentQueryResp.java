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
