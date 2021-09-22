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

import com.hummer.common.utils.DateUtil;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.yug.context.YugBeanPaymentContext;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_PAYMENT_CHANNEL;

@Service(YUG_BEAN_PAYMENT_CHANNEL)
public class YugBeanPayment implements PaymentChannel<YugBeanPaymentContext
        , Integer, PaymentContext, PaymentResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public YugBeanPaymentContext builder(BaseContext<PaymentContext> context) throws Throwable {
        YugBeanPaymentContext paymentContext = new YugBeanPaymentContext();
        paymentContext.setBeanNumber(context.getContext().getBeanNumber());

        return paymentContext;
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
    public Integer doCall(BaseContext<PaymentContext> context
            , YugBeanPaymentContext reqContext) throws Throwable {
        return reqContext.getBeanNumber();
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(Integer resp) throws Throwable {
        PaymentResultContext context = PaymentResultContext.builder()
                .success(true)
                .channelTradeId(String.format("YG%s", DateUtil.formatNowDate("yyyyMMddHHmmssSSS")))
                .paymentStatus(PaymentStatusEnum.PAYMENT_SUCCESS)
                .paymentDateTime(DateUtil.now())
                .build();

        context.setResult(context);

        return context;
    }
}
