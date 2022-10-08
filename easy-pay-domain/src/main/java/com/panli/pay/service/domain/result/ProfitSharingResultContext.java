package com.panli.pay.service.domain.result;

import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.enums.ProfitSharingStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProfitSharingResultContext extends BaseResultContext<ProfitSharingResultContext>{

    private String channelFzOrderId;
    private ProfitSharingStatusEnum status;

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
