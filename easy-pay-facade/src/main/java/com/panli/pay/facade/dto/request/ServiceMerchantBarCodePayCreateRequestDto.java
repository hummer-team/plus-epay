package com.panli.pay.facade.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceMerchantBarCodePayCreateRequestDto extends BasePaymentRequestDto {
        private String subAppId;
        @NotEmpty(message = "sub mch id can not empty")
        private String subMchId;
        @NotNull(message = "profitSharing can not null, valid value is : true or false")
        private Boolean profitSharing;
}
