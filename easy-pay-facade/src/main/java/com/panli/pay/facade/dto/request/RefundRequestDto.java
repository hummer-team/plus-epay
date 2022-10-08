package com.panli.pay.facade.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RefundRequestDto {
    @NotEmpty(message = "tradeId can not empty")
    private String tradeId;
    @NotEmpty(message = "refundBatchId can not empty")
    private String refundBatchId;
    @NotNull(message = "refund amount can not empty")
    private BigDecimal amount;
    private String channelCode;
    @NotEmpty(message = "userId can not empty")
    private String userId;
    private String platformCode;
}
