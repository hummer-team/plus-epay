package com.panli.pay.service.domain.core;

import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lee
 */
@Slf4j
public abstract class AbstractPaymentQueryTemplate extends AbstractTemplate {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PaymentQueryResultContext doQuery(BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto
            , PaymentChannel paymentQuery) {
        PaymentQueryContext context = setContextAndSelfCheck(dto);
        try {
            BasePaymentChannelReqBodyContext reqBodyContext = paymentQuery.builder(context);
            long start = System.currentTimeMillis();
            BaseResultContext<PaymentQueryResultContext> resultContext =
                    paymentQuery.parseResp(paymentQuery.doCall(context, reqBodyContext));

            log.info("payment query `PlatFormCode:{} - ChannelCode:{} - TradeId:{} - req:{} - OriginResp:{} - costMs:{}`"
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
                            , dto.getTradeId(), null, e));
            throw new AppException(50000, String.format("payment query %s failed " + e.getMessage()
                    , context.getChannelCode()), e);
        }
    }

    /**
     * set context
     *
     * @param dto dto
     * @return {@link PaymentQueryContext}
     */
    protected abstract PaymentQueryContext setContextAndSelfCheck(
            BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto);
}
