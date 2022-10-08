package com.panli.pay.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author lee
 */
@Data
public class BaseQueryPaymentStatusRequestDto<T extends BaseQueryPaymentStatusRequestDto<T>> {
    @NotEmpty(message = "trade id can not empt")
    private String tradeId;
    private String platformCode;

    @ApiModelProperty(hidden = true)
    private String channelCode;

    private transient T data;
}
