package com.panli.pay.service.domain.context;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
public class RefundContext extends BaseContext<RefundContext> {
    private String tradeId;
    private String channelTradeId;
    private String refundBatchId;
    private BigDecimal amount;
    private BigDecimal originOrderAmount;
    private String reason;
    private String serviceUrl;

    private String platformCode;
    private String channelCode;
    private Integer orderTag;
    private String platformSubType;
    private String userId;

    private boolean needChannelConfig;
}
