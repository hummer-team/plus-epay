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
