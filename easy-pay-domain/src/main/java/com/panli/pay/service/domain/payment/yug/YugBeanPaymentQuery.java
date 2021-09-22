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

import com.hummer.common.exceptions.AppException;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.context.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.yug.context.YugBeanPaymentQueryContext;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_PAYMENT_QUERY_CHANNEL;

@Service(YUG_BEAN_PAYMENT_QUERY_CHANNEL)
public class YugBeanPaymentQuery implements PaymentChannel<YugBeanPaymentQueryContext
        , PaymentQueryContext
        , PaymentQueryContext
        , PaymentQueryResultContext> {

    @Autowired
    private PaymentOrderDao paymentOrderDao;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public YugBeanPaymentQueryContext builder(BaseContext<PaymentQueryContext> context) throws Throwable {
        YugBeanPaymentQueryContext context1 = new YugBeanPaymentQueryContext();
        context1.setChannelCode(context.getChannelCode());
        context1.setTradeId(context.getContext().getTradeId());
        context1.setChannelCode(context.getContext().getChannelCode());
        //context1.setData(context1);
        return context1;
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
    public PaymentQueryContext doCall(BaseContext<PaymentQueryContext> context
            , YugBeanPaymentQueryContext reqContext) throws Throwable {
        return context.getContext();
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(PaymentQueryContext resp) throws Throwable {
        List<PaymentOrderPo> paymentOrderPos = paymentOrderDao.queryByTradeIdAndCode(resp.getTradeId()
                , resp.getChannelCode());
        if (CollectionUtils.isEmpty(paymentOrderPos)) {
            throw new AppException(40004, "trade id invalid " + resp.getTradeId() + " channelCode " + resp.getChannelCode());
        }
        PaymentOrderPo orderPo = paymentOrderPos.get(0);
        PaymentQueryResultContext context = PaymentQueryResultContext.builder().build();
        context.setTradeId(orderPo.getTradeId());
        context.setChannelTradeId(orderPo.getChannelTradeId());
        context.setStatus(String.valueOf(orderPo.getStatusCode()));
        context.setStatusDesc(PaymentStatusEnum.getByCode(orderPo.getStatusCode()).getMessage());
        context.setResult(context);

        return context;
    }
}
