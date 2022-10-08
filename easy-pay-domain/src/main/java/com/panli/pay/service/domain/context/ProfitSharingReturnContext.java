package com.panli.pay.service.domain.context;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfitSharingReturnContext extends BaseContext<ProfitSharingReturnContext> {
    private String subMchId;
    private String returnMchId;
    private BigDecimal amount;
    private String remark;
    private String profitSharingTradeId;
    private String returnId;
    private String channelPsId;
}
