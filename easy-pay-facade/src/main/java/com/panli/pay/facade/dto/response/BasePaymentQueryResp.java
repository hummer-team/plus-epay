package com.panli.pay.facade.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author edz
 */
@Data
public class BasePaymentQueryResp<T extends BasePaymentQueryResp<?>> {
    public static final BasePaymentQueryResp<?> EMPTY = new BasePaymentQueryResp<>();
    private String tradeId;
    private Integer status;
    private String describe;
    private String channelStatus;
    private String channeldescribe;


    private String channelCode;
    private String channelTradeId;

    private String merchantId;
    private BigDecimal amount;
    private Integer amountUnitType;
}
