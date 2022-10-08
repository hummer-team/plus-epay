package com.panli.pay.support.model.po;

import lombok.Data;

@Data
public class ChannelPlatformConfigPo extends BasePo {
    private String channelPlatform;
    private String platformCode;
    private String platformDescribe;
    private String channelPayQueryCode;
    private String channelPayCode;
    private String channelRefundCode;
    private String channelRefundQueryCode;
    private String channelCancelCode;
    private String businessDescribe;
    private String channelShortCode;
    private String channelAddReceiver;
}
