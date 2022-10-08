package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;

/**
 * @author edz
 */
@Data
public class ServiceMerchantBarCodePayCreateRespDto extends BasePaymentResp<ServiceMerchantBarCodePayCreateRespDto> {
    private String returnCode;
    private String returnMsg;
}
