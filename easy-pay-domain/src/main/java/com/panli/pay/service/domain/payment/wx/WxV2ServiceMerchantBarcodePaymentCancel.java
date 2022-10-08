package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.PaymentCancelContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CANCEL_SERVICE_MCH;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1'>服务商条码支付-创建分账订单</a>
 */
@Service(WX_BARCODE_PAYMENT_CANCEL_SERVICE_MCH)
@Slf4j
public class WxV2ServiceMerchantBarcodePaymentCancel extends WxV2BarcodePaymentCancel {

    @Override
    protected Map<String, Object> putRequestBody(BaseContext<PaymentCancelContext> context, ChannelConfigPo configPo) {
        Map<String, Object> map = super.putRequestBody(context, configPo);
        String subAppId = context.getContext().getSubAppId();
        if (StringUtils.isNotEmpty(subAppId) && !"null".equals(subAppId)) {
            map.put("sub_appid", subAppId);
        }
        map.put("sub_mch_id", context.getContext().getSubMchId());
        return map;
    }

}
