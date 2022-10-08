package com.panli.pay.service.domain.core;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.ProfitSharingAddReceiverResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edz
 */
@Slf4j
public abstract class AbstractProfitSharingAddReceiverTemplate extends AbstractTemplate {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BaseResultContext<ProfitSharingAddReceiverResultContext> doAddReceiver(BaseContext<? extends BaseContext<?>> context
            , PaymentChannel payment) {
        //set config
        setContextAndSelfCheck(context);
        //
        BaseResultContext<ProfitSharingAddReceiverResultContext> result = checkExists(context);
        if (result != null) {
            return result;
        }
        try {
            checkRisk(context);
            //builder request third party service context
            long start = System.currentTimeMillis();
            BasePaymentChannelReqBodyContext reqBody = payment.builder(context);
            //do pay
            //do parse response
            Object channelResp = payment.doCall(context, reqBody);
            result = payment.parseResp(channelResp);
            //save result
            result.setCostTimeMills((int) (System.currentTimeMillis() - start));
            //add log
            log.debug("add receiver done {} - {} - {} - {} ms"
                    , context.getPlatformCode(), context.getChannelCode()
                    , context.getUserId()
                    , result.getCostTimeMills());
            handleResult(context, result);
            freeResourceForPay(context, result);
            return result;
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
     * implement payment verify it before
     *
     * @param context context
     */
    protected abstract void setContextAndSelfCheck(BaseContext<? extends BaseContext<?>> context);

    protected abstract BaseResultContext<ProfitSharingAddReceiverResultContext> checkExists(BaseContext<? extends BaseContext<?>> context);

    /**
     * check payment is exists risk
     *
     * @param context context
     */
    protected abstract void checkRisk(BaseContext<? extends BaseContext<?>> context);

    protected abstract void handleResult(BaseContext<? extends BaseContext<?>> context,
                                         BaseResultContext<ProfitSharingAddReceiverResultContext> result);


}
