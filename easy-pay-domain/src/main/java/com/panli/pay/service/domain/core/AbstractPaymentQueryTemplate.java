/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.panli.pay.service.domain.core;

import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.context.PaymentQueryResultContext;
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
