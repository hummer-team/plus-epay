package com.panli.pay.service.domain.core;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentCancelContext;
import com.panli.pay.service.domain.result.PaymentCancelResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCancelPaymentTemplate extends AbstractTemplate {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PaymentCancelResultContext doCancel(PaymentCancelContext context, PaymentChannel cancel) {

        //before verify
        //set config
        setContextAndSelfCheck(context);

        BaseResultContext<PaymentCancelResultContext> result;
        try {
            checkRisk(context);
            //builder request third party service context
            long start = System.currentTimeMillis();
            BasePaymentChannelReqBodyContext reqBody = cancel.builder(context);
            //do pay
            //do parse response
            Object channelResp = cancel.doCall(context, reqBody);
            result = cancel.parseResp(channelResp);
            //save result
            result.setCostTimeMills((int) (System.currentTimeMillis() - start));
            handleResult(result.getResult(), context, reqBody);
            //add log
            log.debug("payment cancel done {} - {} - {} - {} - {} - {} ms"
                    , context.getPlatformCode(), context.getChannelCode(), context.getTradeId()
                    , context.getUserId()
                    , context.getAffixData()
                    , result.getCostTimeMills());
            freeResourceForPay(context, result);
            return result.getResult();
        } catch (Throwable e) {
            log.error("payment cancel fail,{} - {} - {} - "
                    , context.getPlatformCode(), context.getChannelCode(), context.getTradeId(), e);
            //publish sys event
            SpringApplicationContext.getApplicationContext()
                    .publishEvent(new SysLogEvent(AbstractPaymentTemplate.class
                            , context.getPlatformCode(), context.getPlatformSubType(), context.getChannelCode()
                            , JSON.toJSONString(context)
                            , context.getUserId()
                            , e));

            throw new AppException(50000, "payment failed", e);
        }
    }

    /**
     * Generally, it does not need to be Override
     *
     * @param resultContext  context
     * @param paymentContext payment info
     * @param reqBody        request payment body
     */
    protected abstract void handleResult(PaymentCancelResultContext resultContext
            , PaymentCancelContext paymentContext, BasePaymentChannelReqBodyContext reqBody);

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    protected abstract void setContextAndSelfCheck(PaymentCancelContext context);

    /**
     * check payment is exists risk
     *
     * @param paymentContext paymentContext
     */
    protected abstract void checkRisk(PaymentCancelContext paymentContext);
}
