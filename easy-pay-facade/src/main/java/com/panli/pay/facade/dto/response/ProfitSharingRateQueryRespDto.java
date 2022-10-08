package com.panli.pay.facade.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author edz
 */
@Data
public class ProfitSharingRateQueryRespDto {

    @ApiModelProperty("商户号")
    private String subMchId;

    @ApiModelProperty("最大分润比例")
    private int maxRatio;
}
