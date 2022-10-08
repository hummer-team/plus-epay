package com.panli.pay.support.model.bo;

import lombok.Builder;

import java.util.Date;

@Builder
public class UpdateRefundStatusBo {
    private final String tradeId;
    private final int status;
    private final String channelTradeId;
    private final String channelRefundId;
    private final Date channelRefundDateTime;
    private final String channelRefundStatus;
}
