package com.panli.pay.service.domain.result;

import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class PaymentResultContext extends BaseResultContext<PaymentResultContext> {
    private String requestBody;
    private Date createdDatetime;
    private int systemCode;
    private String systemMessage;

    private BigDecimal payAmount;
    private String channelTradeId;
    private String channelAdvancePaymentId;
    private String tradeId;
    private PaymentStatusEnum paymentStatus;
    private Date paymentDateTime;
}
