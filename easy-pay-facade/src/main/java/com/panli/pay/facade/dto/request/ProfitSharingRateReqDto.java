package com.panli.pay.facade.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * ProfitSharingRateReqDto
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2022</p>
 * @date 2022/9/1 9:25
 */
@Data
public class ProfitSharingRateReqDto extends BaseChannelReqDto<ProfitSharingRateReqDto> {

    @NotEmpty(message = "sub mch id can not empty")
    private String subMchId;
}
