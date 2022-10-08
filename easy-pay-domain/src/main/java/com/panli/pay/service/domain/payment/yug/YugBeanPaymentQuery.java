package com.panli.pay.service.domain.payment.yug;

import com.hummer.common.exceptions.AppException;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.yug.context.YugBeanPaymentQueryContext;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_PAYMENT_QUERY_CHANNEL;

@Service(YUG_BEAN_PAYMENT_QUERY_CHANNEL)
public class YugBeanPaymentQuery implements PaymentChannel<YugBeanPaymentQueryContext
        , PaymentQueryContext
        , PaymentQueryContext
        , PaymentQueryResultContext> {

    @Autowired
    private PaymentOrderDao paymentOrderDao;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public YugBeanPaymentQueryContext builder(BaseContext<PaymentQueryContext> context) throws Throwable {
        YugBeanPaymentQueryContext context1 = new YugBeanPaymentQueryContext();
        context1.setChannelCode(context.getChannelCode());
        context1.setTradeId(context.getContext().getTradeId());
        context1.setChannelCode(context.getContext().getChannelCode());
        //context1.setData(context1);
        return context1;
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
    public PaymentQueryContext doCall(BaseContext<PaymentQueryContext> context
            , YugBeanPaymentQueryContext reqContext) throws Throwable {
        return context.getContext();
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(PaymentQueryContext resp) throws Throwable {
        List<PaymentOrderPo> paymentOrderPos = paymentOrderDao.queryByTradeIdAndCode(resp.getTradeId()
                , resp.getChannelCode());
        if (CollectionUtils.isEmpty(paymentOrderPos)) {
            throw new AppException(40004, "trade id invalid " + resp.getTradeId() + " channelCode " + resp.getChannelCode());
        }
        PaymentOrderPo orderPo = paymentOrderPos.get(0);
        PaymentQueryResultContext context = PaymentQueryResultContext.builder().build();
        context.setTradeId(orderPo.getTradeId());
        context.setChannelTradeId(orderPo.getChannelTradeId());
        context.setStatus(PaymentStatusEnum.getByCode(orderPo.getStatusCode()));
        context.setStatusDesc(PaymentStatusEnum.getByCode(orderPo.getStatusCode()).getMessage());
        context.setResult(context);

        return context;
    }
}
