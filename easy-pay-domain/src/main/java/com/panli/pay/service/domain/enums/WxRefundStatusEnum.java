package com.panli.pay.service.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum WxRefundStatusEnum {
    SUCCESS("退款成功"),
    CLOSE("退款关闭"),
    ABNORMAL("退款异常"),
    UNKNOWN("未知");
    private static final Map<String, WxRefundStatusEnum> map = new HashMap<>();

    static {
        map.put("SUCCESS", WxRefundStatusEnum.SUCCESS);
        map.put("CLOSE", WxRefundStatusEnum.CLOSE);
        map.put("ABNORMAL", WxRefundStatusEnum.ABNORMAL);
        map.put("UNKNOWN", WxRefundStatusEnum.UNKNOWN);
    }

    private final String code;

    WxRefundStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static WxRefundStatusEnum getByCode(String code) {
        return map.getOrDefault(code, WxRefundStatusEnum.UNKNOWN);
    }
}

