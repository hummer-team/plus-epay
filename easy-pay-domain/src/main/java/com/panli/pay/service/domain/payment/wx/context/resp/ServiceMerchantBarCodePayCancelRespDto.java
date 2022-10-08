package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceMerchantBarCodePayCancelRespDto extends BasePaymentResp<ServiceMerchantBarCodePayCancelRespDto> {
    private String returnCode;
    private String returnMsg;
}
