package com.panli.pay.service.domain.enums;

public enum FlowTypeEnum {
    IN((byte) 0),
    OUT((byte) 1);
    private final byte code;

    FlowTypeEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
