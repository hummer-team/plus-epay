package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;

@Data
public class WxV3AdvancePaymentResp extends BasePaymentResp<WxV3AdvancePaymentResp> {
    private String channelTradeId;
    private String channelAdvancePaymentId;
    private String channelCode;
    private String threadId;
}
