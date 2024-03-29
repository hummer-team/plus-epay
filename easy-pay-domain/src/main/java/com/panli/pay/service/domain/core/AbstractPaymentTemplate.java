package com.panli.pay.service.domain.core;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * this is pay template ,{@link #doPayment(PaymentContext, PaymentChannel)} define pay step,
 * child class implement pay logic,the {@link com.panli.pay.service.domain.template.DefaultPaymentTemplate} class implement default pay
 *
 * @author lee
 */
@Slf4j
public abstract class AbstractPaymentTemplate extends AbstractTemplate {
    /**
     * this is payment dispatch.
     * <li>
     * <ul>1,before verify and set config</ul>
     * <ul>2,pay and parse response</ul>
     * <ul>3,result save to db</ul>
     * <ul>4,final</ul>
     * <li>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BasePaymentResp<? extends BasePaymentResp<?>> doPayment(PaymentContext context, PaymentChannel payment) {
        //before verify
        //set config
        setContextAndSelfCheck(context);

        BaseResultContext<PaymentResultContext> result;
        try {
            checkRisk(context);
            //builder request third party service context
            long start = System.currentTimeMillis();
            BasePaymentChannelReqBodyContext reqBody = payment.builder(context);
            //do pay
            //do parse response
            Object channelResp = payment.doCall(context, reqBody);
            result = payment.parseResp(channelResp);
            BasePaymentResp<? extends BasePaymentResp<?>> resultMap = payment.builderRespMessage(result, channelResp);
            //save result
            result.setCostTimeMills((int) (System.currentTimeMillis() - start));
            handleResult(result.getResult(), context, reqBody);
            //add log
            log.debug("payment done {} - {} - {} - {} - {} - {} ms"
                    , context.getPlatformCode(), context.getChannelCode(), context.getOrderTag()
                    , context.getUserId()
                    , context.getAmount()
                    , result.getCostTimeMills());
            freeResourceForPay(context, result);
            return resultMap;
        } catch (Throwable e) {
            log.error("payment fail,{} - {} - {} - "
                    , context.getPlatformCode(), context.getChannelCode(), context.getOrderTag(), e);
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
    protected abstract void handleResult(PaymentResultContext resultContext
            , PaymentContext paymentContext, BasePaymentChannelReqBodyContext reqBody);

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    protected abstract void setContextAndSelfCheck(PaymentContext context);

    /**
     * check payment is exists risk
     *
     * @param paymentContext paymentContext
     */
    protected abstract void checkRisk(PaymentContext paymentContext);
}
