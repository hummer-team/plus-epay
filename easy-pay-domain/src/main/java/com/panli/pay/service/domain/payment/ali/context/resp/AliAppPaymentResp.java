package com.panli.pay.service.domain.payment.ali.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import lombok.Data;

/**
 * @author lee
 */
@Data
public class AliAppPaymentResp extends BasePaymentResp<AliAppPaymentResp> {
    private String paySign;
}
