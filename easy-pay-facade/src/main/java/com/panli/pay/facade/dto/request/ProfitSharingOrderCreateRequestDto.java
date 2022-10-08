package com.panli.pay.facade.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ed
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账，微信：https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml，支付宝：")
public class ProfitSharingOrderCreateRequestDto extends BaseProfitSharingOrderRequestDto<ProfitSharingOrderCreateRequestDto> {
    @NotNull(message = "ProfitSharing Receivers can not null")
    private List<Receivers> receivers;
    private Boolean unfreezeUnSplit = true;

    @ApiModelProperty(name = "分账订单交易ID")
    @NotEmpty(message = "ProfitSharing trade id can not null")
    private String fzTradeId;

    @Data
    @Valid
    public static class Receivers {
        @NotEmpty(message = "type can not null")
        private String type;
        @NotEmpty(message = "account id can not null")
        private String account;
        private String realName;
        @NotNull(message = "amount value can not null")
        private BigDecimal amount;
        @NotEmpty(message = "memo  can not null")
        private String description;
    }
}
