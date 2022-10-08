package com.panli.pay.support.model.bo;

import lombok.Data;

@Data
public class NotifyRequestBo {
    private String notifyId;
    private String channelCode;
    private String body;
    private String decryptBody;
    private String handlerId;
    private String head;
    private String notifyType;
}
