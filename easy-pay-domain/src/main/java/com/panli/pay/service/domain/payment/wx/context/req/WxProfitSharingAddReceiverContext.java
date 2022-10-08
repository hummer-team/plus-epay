package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ProfitSharing request context
 *
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxProfitSharingAddReceiverContext extends BasePaymentChannelReqBodyContext {

    @JSONField(name = "merchantId")
    private transient String merchantId;

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

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
