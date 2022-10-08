package com.panli.pay.facade.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceMerchantBarCodePayQueryRequestDto
        extends BaseQueryPaymentStatusRequestDto<ServiceMerchantBarCodePayQueryRequestDto> {
    private String subAppId;
    @NotEmpty(message = "sub mch id can not empty")
    private String subMchId;
}
