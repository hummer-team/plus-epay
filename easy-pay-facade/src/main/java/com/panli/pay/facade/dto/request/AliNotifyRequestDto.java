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

package com.panli.pay.facade.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AliNotifyRequestDto {
    @JSONField(name = "notify_time")
    private Date notifyTime;
    @JSONField(name = "notify_type")
    private String notifyType;
    @JSONField(name = "notify_id")
    private String notifyId;
    @JSONField(name = "app_id")
    private String appId;
    private String charset;
    private String version;
    @JSONField(name = "sign_type")
    private String signType;
    private String sign;
    @JSONField(name = "trade_no")
    private String tradeNo;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "out_biz_no")
    private String outBizNo;
    @JSONField(name = "buyer_id")
    private String buyerId;
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
    @JSONField(name = "seller_id")
    private String sellerId;
    @JSONField(name = "seller_email")
    private String sellerEmail;
    @JSONField(name = "trade_status")
    private String tradeStatus;
    @JSONField(name = "total_amount")
    private BigDecimal totalAmount;
    @JSONField(name = "receipt_amount")
    private BigDecimal receiptAmount;
    @JSONField(name = "invoice_amount")
    private BigDecimal invoiceAmount;
    @JSONField(name = "buyer_pay_amount")
    private BigDecimal buyerPayAmount;
    @JSONField(name = "point_amount")
    private BigDecimal pointAmount;
    @JSONField(name = "refund_fee")
    private BigDecimal refundFee;
    private String subject;
    private String body;
    @JSONField(name = "gmt_create")
    private Date gmtCreate;
    @JSONField(name = "gmt_payment")
    private Date gmtPayment;
    @JSONField(name = "gmt_refund")
    private Date gmtRefund;
    @JSONField(name = "gmt_close")
    private Date gmtClose;
    @JSONField(name = "fund_bill_list")
    private String fundBillList;
    @JSONField(name = "passback_params")
    private String passbackParams;
    @JSONField(name = "voucher_detail_list")
    private String voucherDetailList;
}
