package com.panli.pay.service.domain.payment.yug;

import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.yug.context.YugBeanRefundContext;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_PAYMENT_REFUND_CHANNEL;

@Service(YUG_BEAN_PAYMENT_REFUND_CHANNEL)
public class YugBeanRefund implements PaymentChannel<YugBeanRefundContext, YugBeanRefundContext, RefundContext
        , RefundResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public YugBeanRefundContext builder(BaseContext<RefundContext> context) throws Throwable {
        YugBeanRefundContext refundContext = new YugBeanRefundContext();
        refundContext.setBeanNumber(context.getContext().getAmount());
        refundContext.setRefundBatchId(context.getContext().getRefundBatchId());
        refundContext.setTradeId(context.getContext().getTradeId());
        refundContext.setChannelTradeId(context.getContext().getChannelTradeId());
        //refundContext.setData(refundContext);

        return refundContext;
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
    public YugBeanRefundContext doCall(BaseContext<RefundContext> context
            , YugBeanRefundContext reqContext) throws Throwable {
        return reqContext;
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(YugBeanRefundContext resp) throws Throwable {
        RefundResultContext context = RefundResultContext.builder().build();
        context.setAmount(resp.getBeanNumber());
        context.setSuccess(true);
        context.setChannelRefundStatus(PaymentStatusEnum.REFUND_SUCCESS.getCode2());
        context.setStatus(PaymentStatusEnum.REFUND_SUCCESS);
        context.setChannelRefundId(resp.getRefundBatchId());
        context.setResult(context);
        return context;
    }
}
