package com.panli.pay.service.domain.context;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfitSharingUnfreezeContext extends BaseContext<ProfitSharingUnfreezeContext> {
    private String subMchId;

    private String channelTradeId;

    private String profitSharingCode;

    private String description;
}
