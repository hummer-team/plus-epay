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
public class PaymentCancelContext extends BaseContext<PaymentCancelContext> {
    private String tradeId;
    private String channelCode;
    private String subMchId;
    private String subAppId;
}
