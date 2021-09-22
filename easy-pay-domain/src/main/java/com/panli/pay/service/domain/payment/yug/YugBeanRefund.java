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

package com.panli.pay.service.domain.payment.yug;

import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.context.RefundResultContext;
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
