package com.panli.pay.support.model.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NotifyDataBo {
    private String tradeId;
    private String channelTradeId;
    private String channelCode;
    private String channelStatus;
    private String channelStatusDescribe;
    private BigDecimal amount;
    private Date dateTime;

    private String paymentRequestId;
    private String refundBatchId;
    private String channelRefundId;
    private int status;
    private String statusDescribe;
    private boolean success;
    private boolean isIn;

    private NotifyRequestBo notifyRequestBo;
}
