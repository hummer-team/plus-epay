package com.panli.pay.service.domain.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class ProfitSharingAddReceiverResultContext extends BaseResultContext<ProfitSharingAddReceiverResultContext> {

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
