package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.RefundContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_REFUND_SERVICE_MCH;

/**
 * @author chenwei
 */
@Service(WX_BARCODE_PAYMENT_REFUND_SERVICE_MCH)
@Slf4j
public class WxV2ServiceMerchantBarcodePaymentRefund extends WxV2Refund {

    @Override
    public Map<String, Object> composeParams(BaseContext<RefundContext> context) {
        Map<String, Object> map = super.composeParams(context);
        map.put("sub_mch_id", context.getContext().getPaymentOrder().getMerchantId());
        return map;
    }
}
