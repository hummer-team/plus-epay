package com.panli.pay.service.domain.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * this is channel pay query result
 *
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PaymentCancelResultContext extends BaseResultContext<PaymentCancelResultContext> {
    private String status;
    private String describe;
    private String statusDesc;
    private String tradeId;
    private String channelTradeId;

    private boolean recall;
}
