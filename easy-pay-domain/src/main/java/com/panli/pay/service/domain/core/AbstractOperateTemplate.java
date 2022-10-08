package com.panli.pay.service.domain.core;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOperateTemplate<T extends BaseContext<T>
        , F extends BaseResultContext<F>, R extends BasePaymentResp<R>> extends AbstractTemplate {
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
    public R doAction(T context, PaymentChannel payment) {
        //before verify
        //set config
        setContextAndSelfCheck(context);

        BaseResultContext<F> result;
        try {
            checkRisk(context);
            //builder request third party service context
            long start = System.currentTimeMillis();
            BasePaymentChannelReqBodyContext reqBody = payment.builder(context);
            //do pay
            //do parse response
            Object channelResp = payment.doCall(context, reqBody);
            result = payment.parseResp(channelResp);
            BasePaymentResp<R> resultMap = payment.builderRespMessage(result, channelResp);
            //save result
            result.setCostTimeMills((int) (System.currentTimeMillis() - start));
            handleResult(result.getResult(), context, reqBody);
            //add log
            log.debug("payment done {} - {} - {} - {} - {} ms"
                    , context.getPlatformCode(), context.getChannelCode(), context.getPlatformSubType()
                    , context.getUserId(), result.getCostTimeMills());
            freeResourceForPay(context, result);
            return (R) resultMap;
        } catch (Throwable e) {
            log.error("payment fail,{} - {} - {} - "
                    , context.getPlatformCode(), context.getChannelCode(), context.getPlatformSubType(), e);
            //publish sys event
            SpringApplicationContext.getApplicationContext()
                    .publishEvent(new SysLogEvent(AbstractOperateTemplate.class
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
    protected abstract void handleResult(F resultContext
            , T paymentContext, BasePaymentChannelReqBodyContext reqBody);

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    protected abstract void setContextAndSelfCheck(T context);

    /**
     * check payment is exists risk
     *
     * @param paymentContext paymentContext
     */
    protected abstract void checkRisk(T paymentContext);
}
