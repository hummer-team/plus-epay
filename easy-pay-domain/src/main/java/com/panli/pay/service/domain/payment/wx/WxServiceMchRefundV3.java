package com.panli.pay.service.domain.payment.wx;

import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.payment.wx.context.req.WxRefundV3ReqDto;
import com.panli.pay.service.domain.payment.wx.context.req.WxServiceMchRefundV3ReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_SERVICE_PAYMENT_REFUND;

/**
 * @author chenwei
 */
@Service(WX_SERVICE_PAYMENT_REFUND)
@Slf4j
public class WxServiceMchRefundV3 extends WxV3Refund {

    @Override
    public WxRefundV3ReqDto builder(BaseContext<RefundContext> context) {
        WxServiceMchRefundV3ReqDto dto = ObjectCopyUtils.copy(super.builder(context), WxServiceMchRefundV3ReqDto.class);
        dto.setSubMchId(context.getPaymentOrder().getMerchantId());
        return dto;
    }

}
