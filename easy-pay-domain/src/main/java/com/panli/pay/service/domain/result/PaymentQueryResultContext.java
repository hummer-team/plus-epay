package com.panli.pay.service.domain.result;

import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * this is channel pay query result
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PaymentQueryResultContext extends BaseResultContext<PaymentQueryResultContext> {
    private PaymentStatusEnum status;
    private String describe;
    private String statusDesc;
    private String tradeId;
    private String channelTradeId;

}
