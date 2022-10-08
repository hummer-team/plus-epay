package com.panli.pay.integration.wxpayment.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.TreeMap;

@Data
@SuperBuilder
public class BaseWxRequest<T extends BaseWxRequest<T>> {
    private T request;
    private String appId;
    private String merchantId;
    private String signKey;
    private Integer connTimeoutMillis;
    private Integer redTimeoutMillis;
    @Builder.Default
    private TreeMap<String, Object> parameter = new TreeMap<>();

    public TreeMap<String, Object> getParameter() {
        return parameter;
    }

    public BaseWxRequest<T> addParameter(String name, Object val) {
        parameter.put(name, val);
        return this;
    }
}
