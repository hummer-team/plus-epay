package com.panli.pay.support.model.po;

import com.alibaba.fastjson.JSONObject;
import com.hummer.core.exceptions.KeyNotExistsException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class ChannelConfigPo extends BasePo {
    private String channelCode;
    private String merchantId;
    private String channelName;
    private String publicKey;
    private String privateKey;
    private String extendParameter;
    private String serviceUrl;
    private Integer connectTimeoutMs;
    private Integer redTimeoutMs;
    private String appId;

    private ChannelCertPo channelCertPo;

    private ChannelPlatformConfigPo platformConfig;

    private Map<String, String> extraMap;

    public String getExtParameterVal(String key) {
        if (StringUtils.isEmpty(extendParameter)) {
            return null;
        }
        if (extraMap == null) {
            extraMap = JSONObject.parseObject(extendParameter, Map.class);
        }
        if (extraMap != null) {
            return extraMap.get(key);
        }
        return null;
    }

    public String getExtParameterValWithAssertNotNull(String key) {
        String val = getExtParameterVal(key);
        if (StringUtils.isEmpty(val)) {
            throw new KeyNotExistsException(40004, key + " not fund.");
        }
        return val;
    }
}
