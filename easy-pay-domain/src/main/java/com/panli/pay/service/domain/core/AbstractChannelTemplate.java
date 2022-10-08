package com.panli.pay.service.domain.core;

import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.facade.dto.request.BaseChannelReqDto;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwei
 */
@Slf4j
public abstract class AbstractChannelTemplate<T extends BaseChannelReqDto<T>, C extends BaseContext<C>,
        F extends BaseResultContext<F>> extends AbstractTemplate {

    public F doAction(T dto, PaymentChannel paymentChannel) {
        C context = setContextAndSelfCheck(dto);
        try {
            BasePaymentChannelReqBodyContext reqBodyContext = paymentChannel.builder(context);
            long start = System.currentTimeMillis();
            BaseResultContext<F> resultContext =
                    paymentChannel.parseResp(paymentChannel.doCall(context, reqBodyContext));

            log.debug("payment query `PlatFormCode:{} - ChannelCode:{} - TradeId:{} - req:{} - OriginResp:{} - costMs:{}`"
                    , context.getPlatformCode(), context.getChannelCode()
                    , context.getTradeId(), reqBodyContext.requestBody()
                    , resultContext.getChannelOriginResponse(), System.currentTimeMillis() - start);

            resultContext.setCostTimeMills((int) (System.currentTimeMillis() - start));
            return resultContext.getResult();
        } catch (Throwable e) {
            log.error("payment query fail,{} - {} - {} -"
                    , context.getPlatformCode(), context.getChannelCode(), context.getPlatformSubType(), e);
            //publish sys event
            SpringApplicationContext.getApplicationContext()
                    .publishEvent(new SysLogEvent(AbstractPaymentTemplate.class
                            , context.getPlatformCode(), context.getPlatformSubType(), context.getChannelCode()
                            , null, null, e));
            throw new AppException(50000, String.format("payment query %s failed " + e.getMessage()
                    , context.getChannelCode()), e);
        }
    }


    protected abstract C setContextAndSelfCheck(T dto);
}
