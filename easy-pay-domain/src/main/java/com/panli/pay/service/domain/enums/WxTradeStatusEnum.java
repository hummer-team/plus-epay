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

import java.util.HashMap;
import java.util.Map;

public enum WxTradeStatusEnum {
    SUCCESS("支付成功"),
    REFUND("转入退款"),
    NOTPAY("未支付"),
    CLOSED("已关闭"),
    REVOKED("已撤销（付款码支付）"),
    USERPAYING("用户支付中"),
    PAYERROR("支付失败"),
    UNKNOWN("未知");
    private static final Map<String, WxTradeStatusEnum> map = new HashMap<>();

    static {
        map.put("SUCCESS", SUCCESS);
        map.put("REFUND", REFUND);
        map.put("NOTPAY", NOTPAY);
        map.put("CLOSED", CLOSED);
        map.put("REVOKED", REVOKED);
        map.put("USERPAYING", USERPAYING);
        map.put("PAYERROR", PAYERROR);
    }

    private final String code;

    WxTradeStatusEnum(String code) {
        this.code = code;
    }

    public static WxTradeStatusEnum getByCode(String code) {
        return map.getOrDefault(code, UNKNOWN);
    }

    public String getCode() {
        return code;
    }
}
