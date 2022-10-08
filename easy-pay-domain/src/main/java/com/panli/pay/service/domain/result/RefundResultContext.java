package com.panli.pay.service.domain.result;

import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class RefundResultContext extends BaseResultContext<RefundResultContext> {
    private BigDecimal amount;
    private String responseCode;

    private String channelRefundStatus;
    private PaymentStatusEnum status;
    private String channelRefundId;
}
