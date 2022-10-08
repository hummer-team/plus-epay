package com.panli.pay.facade.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BasePaymentCancelRequestDto {
    @NotEmpty(message = "tradeId can not empty")
    private String tradeId;
    @NotEmpty(message = "userId can not empty")
    private String userId;
    private String platformCode;
    private String channelCode;

    private String subAppId;
    @NotEmpty(message = "sub mch id can not empty")
    private String subMchId;
}
