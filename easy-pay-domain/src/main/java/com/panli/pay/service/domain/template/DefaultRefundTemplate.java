package com.panli.pay.service.domain.template;

import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.core.AbstractRefundTemplate;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_REFUND_TEMPLATE;

@Service(DEFAULT_REFUND_TEMPLATE)
@Slf4j
public class DefaultRefundTemplate extends AbstractRefundTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;

    @Override
    protected void checkRisk(RefundContext refundContext) {
        //todo
    }

    @Override
    protected void saveRefundResult(BaseResultContext<RefundResultContext> context
            , RefundContext refundContext
            , BasePaymentChannelReqBodyContext reqBodyContext) {
        resultInfoService.saveRefundResult(context.getResult()
                , refundContext
                , reqBodyContext.requestBody());
    }

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    @Override
    protected void setContextAndSelfCheck(RefundContext context) {
        super.setContextAndSelfCheck(context);
    }
}
