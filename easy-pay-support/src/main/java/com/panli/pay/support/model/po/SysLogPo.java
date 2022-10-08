package com.panli.pay.support.model.po;

import lombok.Data;

@Data
public class SysLogPo extends BasePo {
    private Integer id;
    private String platformCode;
    private String businessCode;
    private String channelCode;
    private Integer sysCode;
    private String sysMessage;
    private String requestId;
    private String requestBody;
}
