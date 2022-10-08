package com.panli.pay.service.domain.context;

import lombok.Data;

/**
 * @author edz
 */
@Data
public abstract class BasePaymentChannelReqBodyContext {

    private transient String serviceUrl;
    private transient int timeoutMillis;
    private transient int retry;

    public abstract String requestBody();
}
