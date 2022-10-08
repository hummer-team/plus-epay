package com.panli.pay.service.domain.enums;

public enum AliTradeStatusEnum {
    WAIT_BUYER_PAY,
    TRADE_CLOSED,
    TRADE_SUCCESS,
    TRADE_FINISHED,
    UNKNOWN;

    public static AliTradeStatusEnum getByCode(String trade) {
        for (AliTradeStatusEnum statusEnum : AliTradeStatusEnum.values()) {
            if (statusEnum.name().equalsIgnoreCase(trade)) {
                return statusEnum;
            }
        }

        return UNKNOWN;
    }
}
