package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxBarCodePaymentReturnResp extends BasePaymentResp<WxBarCodePaymentReturnResp> {
    private String channelRespMessage;

    private String msg;
}
