package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceMerchantBarCodePayQueryRespDto extends BasePaymentQueryResp<ServiceMerchantBarCodePayQueryRespDto> {
    private String returnCode;
    private String returnMsg;

    private String appId;
    private String mchId;
    private String subAppId;
    private String subMchId;
    private String resultCode;
    private String errCode;
    private String errCodeDes;
}
