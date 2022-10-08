package com.panli.pay.support.model.po;

import lombok.Data;

@Data
public class NotifyRecordPo extends BasePo{
    private String notifyId;
    private String channelCode;
    private String body;
    private String handlerId;
    private String head;
    private String  notifyType;
}
