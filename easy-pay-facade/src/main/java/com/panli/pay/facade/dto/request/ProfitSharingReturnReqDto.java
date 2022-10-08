package com.panli.pay.facade.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProfitSharingReturnReqDto extends BaseChannelReqDto<ProfitSharingReturnReqDto> {
    @NotEmpty(message = "sub mch id can not null")
    private String subMchId;
    @NotEmpty(message = "return mch id can not null")
    private String returnMchId;
    @NotNull(message = "amount value can not null")
    private BigDecimal amount;
    @NotEmpty(message = "remark  can not null")
    private String remark;
    @NotEmpty(message = "profitSharingTradeId  can not null")
    private String profitSharingTradeId;
    @NotEmpty(message = "returnNo  can not null")
    private String returnId;

    private String channelPsId;
}
