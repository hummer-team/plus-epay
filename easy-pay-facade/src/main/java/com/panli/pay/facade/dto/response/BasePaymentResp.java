package com.panli.pay.facade.dto.response;

import lombok.Data;

@Data
public class BasePaymentResp<T extends BasePaymentResp<?>> {
    public static final BasePaymentResp<?> EMPTY = new BasePaymentResp<>();

    private String tradeId;
    private Integer status;
    private String statusDesc;
    private String channelTradeId;
}
