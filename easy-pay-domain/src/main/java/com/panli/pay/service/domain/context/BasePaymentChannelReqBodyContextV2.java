package com.panli.pay.service.domain.context;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author edz
 */
@Data
public abstract class BasePaymentChannelReqBodyContextV2<T extends BasePaymentChannelReqBodyContextV2<T>> {
    private Map<String, Object> parameter = new TreeMap<>();
    private String serviceUrl;
    private int timeoutMillis;
    private int retry;
    private transient T data;

    public abstract String requestBody();
}
