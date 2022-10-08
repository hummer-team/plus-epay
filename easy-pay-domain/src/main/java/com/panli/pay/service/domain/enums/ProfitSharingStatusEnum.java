package com.panli.pay.service.domain.enums;

public enum ProfitSharingStatusEnum {
    PROCESSING(0, "PROCESSING", "处理中"),
    FINISHED(1, "FINISHED", "分账完成"),
    UNKNOWN(-99, "unknown", "未知");


    private String describe;
    private String channelCode;
    private int code;

    ProfitSharingStatusEnum(int code, String channelCode, String describe) {
        this.describe = describe;
        this.code = code;
        this.channelCode = channelCode;
    }

    public static ProfitSharingStatusEnum getByChannelCode(String channelCode) {
        for (ProfitSharingStatusEnum statusEnum : ProfitSharingStatusEnum.values()) {
            if (statusEnum.channelCode.equalsIgnoreCase(channelCode)) {
                return statusEnum;
            }
        }

        return UNKNOWN;
    }

    public String getDescribe() {
        return describe;
    }

    public int getCode() {
        return code;
    }

    public String getChannelCode() {
        return channelCode;
    }
}
