package com.panli.pay.service.domain.payment.wx.context.resp;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WxResponse {
    private final boolean success;
    private final String code;
    private final String message;
}
