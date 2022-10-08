package com.panli.pay.support.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentOrderPo {
    private Integer id;
    private String platformCode;
    private String platformSubType;
    private String channelTradeId;
    private Integer payChannelType;
    private BigDecimal amount;
    private String paymentUserId;
    private Date createdDatetime;
    private Boolean isDeleted;
    //private Integer amountUnitType;
    //private Double rate;
    private String tradeId;
    private String requestId;
    private Integer statusCode;
    private String channelCode;
    private Integer orderTag;
    private String refundBatchId;
    private String channelAdvancePaymentId;
    private Date paymentDateTime;
    private Date paymentTimeout;
    private String channelRefundId;

    private String channelRefundStatus;
    private String channelTradeStatus;
    private String merchantId;
    private Integer orderType;
    private Integer payType;

    private Date createdDateTime;
}
