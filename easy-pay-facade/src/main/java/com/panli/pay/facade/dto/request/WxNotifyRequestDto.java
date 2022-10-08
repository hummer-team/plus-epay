package com.panli.pay.facade.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxNotifyRequestDto {
    private String id;
    @JSONField(name = "create_time")
    private String createTime;
    @JSONField(name = "event_type")
    private String eventType;
    @JSONField(name = "resource_type")
    private String resourceType;
    private Resource resource;
    private String summary;

    @Data
    public static class Resource {
        private String algorithm;
        private String ciphertext;
        @JSONField(name = "associated_data")
        private String associatedData;
        @JSONField(name = "original_type")
        private String originalType;
        private String nonce;
    }
}
