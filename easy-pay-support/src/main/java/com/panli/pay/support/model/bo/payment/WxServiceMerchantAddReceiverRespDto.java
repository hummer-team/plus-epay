package com.panli.pay.support.model.bo.payment;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author edz
 */
@Data
public class WxServiceMerchantAddReceiverRespDto {

    private Boolean success;

    private String channelMsg;

    @JSONField(name = "merchantId")
    private String merchantId;

    @JSONField(name = "appid")
    private String appId;

    @JSONField(name = "sub_appid")
    private String subAppId;

    @JSONField(name = "sub_mchid")
    private String subMchId;

    private String type;

    private String account;

    private String name;

    @JSONField(name = "relation_type")
    private String relationType;

    @JSONField(name = "custom_relation")
    private String customRelation;
}
