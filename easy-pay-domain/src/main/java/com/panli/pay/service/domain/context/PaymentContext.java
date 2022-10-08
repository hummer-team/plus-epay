package com.panli.pay.service.domain.context;

import com.panli.pay.service.domain.enums.OrderTypeEnum;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class PaymentContext extends BaseContext<PaymentContext> {

    private BigDecimal amount;
    private Integer beanNumber;
    private Integer orderTag;

    private Integer amountUnitType;

    private PayChannelTypeEnum payChannelType;
    private String remark;
    private String terminalIp;

    private String channelTradeId;
    private String batchId;
//    private String merchantId;
    private OrderTypeEnum orderType;

    private Date paymentTimeOut;

    @Override
    public String toString() {
        return "PaymentContext [" +
                "amount=" + amount +
                ", beanNumber=" + beanNumber +
                ", orderTag=" + orderTag +
                ", amountUnitType=" + amountUnitType +
                ", payChannelType=" + payChannelType +
                ", remark=" + remark +
                ", terminalIp=" + terminalIp +
                ", channelTradeId=" + channelTradeId +
                ", batchId=" + batchId +
                ", super=" + super.toString() +
                ']';
    }
}
