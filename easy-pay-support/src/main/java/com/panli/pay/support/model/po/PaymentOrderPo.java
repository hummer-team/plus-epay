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

package com.panli.pay.support.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentOrderPo {
    private Integer id;
    private String platformCode;
    private String platformSubType;
    private String channelTradeId;
    private Integer payChannelType;
    private BigDecimal amount;
    private String paymentUserId;
    private Date createdDatetime;
    private Boolean isDeleted;
    //private Integer amountUnitType;
    //private Double rate;
    private String tradeId;
    private String requestId;
    private Integer statusCode;
    private  String channelCode;
    private Integer orderTag;
    private String refundBatchId;
    private String channelAdvancePaymentId;
    private Date paymentDateTime;
    private Date paymentTimeout;
    private String channelRefundId;

    private String channelRefundStatus;
    private String channelTradeStatus;
    private String merchantId;
    private Integer orderType;
    private Integer payType;
}
