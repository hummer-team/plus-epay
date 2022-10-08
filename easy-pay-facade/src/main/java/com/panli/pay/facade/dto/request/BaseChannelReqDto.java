package com.panli.pay.facade.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author lee
 */
@Data
public class BaseChannelReqDto<T extends BaseChannelReqDto<T>> {

    @NotEmpty
    private String platformCode;
    @NotEmpty
    private String channelCode;

    private transient T data;
}
