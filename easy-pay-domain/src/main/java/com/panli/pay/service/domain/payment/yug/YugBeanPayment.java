package com.panli.pay.service.domain.payment.yug;

import com.hummer.common.utils.DateUtil;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.yug.context.YugBeanPaymentContext;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_PAYMENT_CHANNEL;

@Service(YUG_BEAN_PAYMENT_CHANNEL)
public class YugBeanPayment implements PaymentChannel<YugBeanPaymentContext
        , Integer, PaymentContext, PaymentResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public YugBeanPaymentContext builder(BaseContext<PaymentContext> context) throws Throwable {
        YugBeanPaymentContext paymentContext = new YugBeanPaymentContext();
        paymentContext.setBeanNumber(context.getContext().getBeanNumber());

        return paymentContext;
    }

    /**
     * call channel service
     *
     * @param context
     * @param reqContext
     * @return
     * @throws Throwable
     */
    @Override
    public Integer doCall(BaseContext<PaymentContext> context
            , YugBeanPaymentContext reqContext) throws Throwable {
        return reqContext.getBeanNumber();
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(Integer resp) throws Throwable {
        PaymentResultContext context = PaymentResultContext.builder()
                .success(true)
                .channelTradeId(String.format("YG%s", DateUtil.formatNowDate("yyyyMMddHHmmssSSS")))
                .paymentStatus(PaymentStatusEnum.PAYMENT_SUCCESS)
                .paymentDateTime(DateUtil.now())
                .build();

        context.setResult(context);

        return context;
    }
}
