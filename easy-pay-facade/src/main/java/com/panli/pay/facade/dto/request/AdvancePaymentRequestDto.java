package com.panli.pay.facade.dto.request;

import lombok.Data;

@Data
public class AdvancePaymentRequestDto extends BasePaymentRequestDto {
    private String openId;
}
