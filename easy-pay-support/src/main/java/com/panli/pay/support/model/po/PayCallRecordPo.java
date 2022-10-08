package com.panli.pay.support.model.po;

import lombok.Data;

import java.util.Date;

@Data
public class PayCallRecordPo extends BasePo {

    private String platformCode;
    private String businessCode;
    private String channelCode;
    private Integer channelType;
    private String requestBody;
    private String originRespBody;
    private String responseCode;
    private String responseMessage;
    private Date createdDatetime;
    private String requestId;
    private Integer systemCode;
    private String systemMessage;

    private Integer callCostTimeMills;
    private String tradeId;
    private String batchId;
}
