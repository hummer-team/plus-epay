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
public class ProfitSharingUnfreezeReqDto extends BaseChannelReqDto<ProfitSharingUnfreezeReqDto> {

    @NotEmpty(message = "sub mch id can not empty")
    private String subMchId;

    @NotEmpty(message = "channelTradeId can not empty")
    private String channelTradeId;

    @NotEmpty(message = "profitSharingCode can not empty")
    private String profitSharingCode;

    @NotEmpty(message = "description can not empty")
    private String description;
}
