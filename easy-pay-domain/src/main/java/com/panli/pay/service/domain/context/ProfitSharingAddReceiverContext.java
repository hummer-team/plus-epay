package com.panli.pay.service.domain.context;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfitSharingAddReceiverContext extends BaseContext<ProfitSharingAddReceiverContext> {

    private String channelCode;

    private String platformCode;

    private String subAppId;

    private String subMchId;

    private String type;

    private String account;

    private String name;

    private String relationType;

    private String customRelation;
}
