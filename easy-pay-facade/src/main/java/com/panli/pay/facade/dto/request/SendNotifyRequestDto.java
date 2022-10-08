package com.panli.pay.facade.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class SendNotifyRequestDto {
    private String notifyType;
    private Integer status;
    private String statusDescribe;
    private Date dateTime;
    private String tradeId;
}
