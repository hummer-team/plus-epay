package com.panli.pay.service.domain.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfitSharingRateQueryResult extends BaseResultContext<ProfitSharingRateQueryResult> {

    @JSONField(name = "sub_mchid")
    private String subMchId;
    @JSONField(name = "max_ratio")
    private int maxRatio;
}
