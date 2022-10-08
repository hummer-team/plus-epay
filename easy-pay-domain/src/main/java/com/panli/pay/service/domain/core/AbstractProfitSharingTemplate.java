package com.panli.pay.service.domain.core;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.ProfitSharingResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edz
 */
@Slf4j
public abstract class AbstractProfitSharingTemplate extends AbstractTemplate {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BasePaymentResp<? extends BasePaymentResp<?>> doPayment(BaseContext<? extends BaseContext<?>> context
            , PaymentChannel payment) {
        //set config
        setContextAndSelfCheck(context);
        //
        checkReceiver(context);

        BaseResultContext<ProfitSharingResultContext> result;
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
            savePaymentResult(result.getResult(), context, reqBody);
            //add log
            log.debug("payment done {} - {} - {} - {} ms"
                    , context.getPlatformCode(), context.getChannelCode()
                    , context.getUserId()
                    , result.getCostTimeMills());
            freeResourceForPay(context, result);
            return resultMap;
        } catch (Throwable e) {
            log.error("payment fail,{} - {} - "
                    , context.getPlatformCode(), context.getChannelCode(), e);
            //publish sys event
            SpringApplicationContext.getApplicationContext()
                    .publishEvent(new SysLogEvent(AbstractPaymentTemplate.class
                            , context.getPlatformCode(), context.getPlatformSubType(), context.getChannelCode()
                            , JSON.toJSONString(context)
                            , context.getUserId()
                            , e));

            throw new AppException(50000, "payment failed " + e.getMessage(), e);
        }
    }

    /**
     * Generally, it does not need to be Override
     *
     * @param resultContext context
     * @param context       payment info
     * @param reqBody       request payment body
     */
    protected abstract void savePaymentResult(ProfitSharingResultContext resultContext
            , BaseContext<? extends BaseContext<?>> context, BasePaymentChannelReqBodyContext reqBody);

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    protected abstract void setContextAndSelfCheck(BaseContext<? extends BaseContext<?>> context);

    protected abstract void checkReceiver(BaseContext<? extends BaseContext<?>> context);

    /**
     * check payment is exists risk
     *
     * @param context context
     */
    protected abstract void checkRisk(BaseContext<? extends BaseContext<?>> context);
}
