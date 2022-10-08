package com.panli.pay.service.domain.payment.ali.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;

@Data
public class AliPaymentH5Resp extends BasePaymentResp<AliPaymentH5Resp> {
    private String paySign;
}
