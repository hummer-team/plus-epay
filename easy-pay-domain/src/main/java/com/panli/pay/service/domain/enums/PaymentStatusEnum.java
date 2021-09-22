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

package com.panli.pay.service.domain.enums;

/**
 * @author edz
 */
public enum PaymentStatusEnum {
    WAIT_PAYMENT(0, "待支付", "WAIT_PAYMENT"),
    PAYMENT_SUCCESS(1, "支付成功", "PAYMENT_SUCCESS"),
    PAYMENT_TIMEOUT(2, "支付超时", "PAYMENT_TIMEOUT"),
    PAYMENT_FAILED(3, "支付失败", "PAYMENT_FAILED"),
    PAYMENT_CLOSED(4, "支付关闭", "PAYMENT_CLOSED"),
    REFUND_ING(5, "退款中", "REFUND_ING"),
    REFUND_SUCCESS(6, "退款成功", "REFUND_SUCCESS"),
    REFUND_FAILED(7, "退款失败", "REFUND_FAILED"),
    
    UNKNOWN(50000, "未知状态", "UNKNOWN");

    // TODO: 2021/7/28 增加部分退款状态
    private final int code;
    private final String message;
    private final String code2;

    PaymentStatusEnum(int code, String message, String code2) {
        this.code = code;
        this.message = message;
        this.code2 = code2;
    }

    public static PaymentStatusEnum getByWxRefundStatus(WxRefundStatusEnum status) {
        if (status == WxRefundStatusEnum.SUCCESS) {
            return REFUND_SUCCESS;
        }
        return REFUND_FAILED;
    }

    public static PaymentStatusEnum getByWxPaymentStatus(WxTradeStatusEnum stats) {
        if (stats == WxTradeStatusEnum.SUCCESS) {
            return PAYMENT_SUCCESS;
        }
        if (stats == WxTradeStatusEnum.NOTPAY || stats == WxTradeStatusEnum.USERPAYING) {
            return WAIT_PAYMENT;
        }
        if (stats == WxTradeStatusEnum.CLOSED) {
            return PAYMENT_CLOSED;
        }
        return PAYMENT_FAILED;
    }

    public static PaymentStatusEnum getByAliPayStatus(AliTradeStatusEnum status) {
        if (status == AliTradeStatusEnum.TRADE_SUCCESS) {
            return PAYMENT_SUCCESS;
        }
        if (status == AliTradeStatusEnum.WAIT_BUYER_PAY) {
            return WAIT_PAYMENT;
        }
        if (status == AliTradeStatusEnum.TRADE_CLOSED) {
            return PAYMENT_CLOSED;
        }
        if (status == AliTradeStatusEnum.TRADE_FINISHED) {
            return PAYMENT_SUCCESS;
        }
        return PAYMENT_FAILED;
    }

    public static PaymentStatusEnum getByCode(Integer code) {
        for (PaymentStatusEnum statusEnum : PaymentStatusEnum.values()) {
            if (statusEnum.code == code) {
                return statusEnum;
            }
        }
        return UNKNOWN;
    }

    public String getCode2() {
        return code2;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PaymentStatusEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
