package com.panli.pay.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author edz
 */
@Data
public class BaseProfitSharingOrderRequestDto<T extends BaseProfitSharingOrderRequestDto<T>> {
    @NotEmpty(message = "sub mch id can not null")
    private String subMchId;
    private String subAppId;
    private String platformCode;
    @NotEmpty(message = "channelCode can not null")
    private String channelCode;
    @NotEmpty(message = "trade id can not empty")
    private String tradeId;
    private String userId;

    @ApiModelProperty(hidden = true)
    private transient T data;

    private Map<String, Object> affixData = new ConcurrentHashMap<>();
}
