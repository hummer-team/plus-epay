package com.panli.pay.service.domain.payment.ali.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;

@Data
public class AliPaymentPcResp extends BasePaymentResp<AliPaymentPcResp> {
    private String paySign;
}
