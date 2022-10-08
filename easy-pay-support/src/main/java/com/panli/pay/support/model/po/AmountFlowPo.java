package com.panli.pay.support.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AmountFlowPo {
    private Integer id;
    private String merchantId;
    private String tradeId;
    private String channelTradeId;
    private String batchId;
    private BigDecimal amount;
    private Boolean flowType;
    private Integer amountUnitType;
    private Double rate;
    private Date createdDatetime;
    private String requestId;
}
