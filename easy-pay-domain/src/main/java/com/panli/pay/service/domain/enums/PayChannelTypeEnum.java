package com.panli.pay.service.domain.enums;

import org.apache.commons.lang3.StringUtils;

public enum PayChannelTypeEnum {
    WEI_XIN(1),
    ALI_PAY(2),
    YUG_NEAN(3),
    CASH(9),
    POST_OFFICE(7);

    private int code;

    PayChannelTypeEnum(int code) {
        this.code = code;
    }

    public static PayChannelTypeEnum getByChannelCode(String channelCode) {
        if (StringUtils.startsWithIgnoreCase(channelCode, "wx")) {
            return WEI_XIN;
        }

        if (StringUtils.startsWithIgnoreCase(channelCode, "ali")) {
            return ALI_PAY;
        }

        if (StringUtils.startsWithIgnoreCase(channelCode, "yug")) {
            return YUG_NEAN;
        }

        throw new IllegalArgumentException("invalid channel code " + channelCode);
    }

    public int getCode() {
        return code;
    }
}
