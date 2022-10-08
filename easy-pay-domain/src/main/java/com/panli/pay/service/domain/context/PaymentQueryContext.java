package com.panli.pay.service.domain.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class PaymentQueryContext extends BaseContext<PaymentQueryContext> {
    private String tradeId;
    private String channelCode;
    private String channelTradeId;
    private String refundBatchId;
    private String channelRefundId;

    private String subMchId;
    private String subAppId;
}
