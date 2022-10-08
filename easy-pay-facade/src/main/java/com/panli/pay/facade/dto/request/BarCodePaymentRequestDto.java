package com.panli.pay.facade.dto.request;

import lombok.Data;

@Data
public class BarCodePaymentRequestDto extends BasePaymentRequestDto {
    private String barcode;
}
