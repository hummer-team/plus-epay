package com.panli.pay.service.domain.event;

import org.slf4j.MDC;
import org.springframework.context.ApplicationEvent;

import static com.hummer.common.SysConstant.REQUEST_ID;

public class SysLogEvent extends ApplicationEvent {

    private String platformCode;
    private String platformSubType;
    private String channelCode;
    private String requestId;
    private String requestBody;
    private String userId;

    private Throwable e;

    public SysLogEvent(Object source
            , String platformCode
            , String platformSubType
            , String channelCode
            , String requestBody
            , String userId
            , Throwable e) {
        super(source);
        this.platformCode = platformCode;
        this.platformSubType = platformSubType;
        this.channelCode = channelCode;
        this.requestId = MDC.get(REQUEST_ID);
        this.requestBody = requestBody;
        this.userId = userId;
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public String getPlatformSubType() {
        return platformSubType;
    }

    public String getChannelCode() {
        return channelCode;
    }


    public String getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequestBody() {
        return requestBody;
    }
}
