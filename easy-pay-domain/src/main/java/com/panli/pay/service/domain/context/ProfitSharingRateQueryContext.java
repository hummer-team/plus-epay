package com.panli.pay.service.domain.context;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfitSharingRateQueryContext extends BaseContext<ProfitSharingRateQueryContext> {
    private String subMchId;
}
