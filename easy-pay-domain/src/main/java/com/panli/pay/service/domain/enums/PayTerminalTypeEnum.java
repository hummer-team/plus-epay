package com.panli.pay.service.domain.enums;

public enum PayTerminalTypeEnum {
    PC(0),
    MOBILE_SCAN_CODE(1),
    MOBILE_BAR_CODE(2),
    ENUM(3)
    ;
    private int code;

    PayTerminalTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
