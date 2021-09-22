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

package com.panli.pay.service.domain.template;

import com.hummer.common.exceptions.AppException;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.core.AbstractPaymentQueryTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_QUERY_TEMPLATE;

@Service(DEFAULT_QUERY_TEMPLATE)
public class DefaultPaymentQueryTemplate extends AbstractPaymentQueryTemplate {
    @Autowired
    private PaymentOrderDao paymentOrderDao;

    @Override
    protected PaymentQueryContext setContextAndSelfCheck(
            BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto) {
        PaymentOrderPo orderPo = paymentOrderDao.queryOneByTradeIdAndCode(dto.getTradeId(), dto.getChannelCode());
        if (orderPo == null) {
            throw new AppException(40004, "trade id invalid " + dto.getTradeId());
        }

        ChannelConfigPo channelConfigPo = checkChannelIsValid(orderPo.getPlatformCode(), dto.getChannelCode()
                , ChannelActionEnum.PAY_QUERY);

        PaymentQueryContext context = PaymentQueryContext.builder()
                .platformCode(orderPo.getPlatformCode())
                .channelCode(orderPo.getChannelCode())
                .platformSubType(orderPo.getPlatformSubType())
                .tradeId(dto.getTradeId())
                .channelConfigPo(channelConfigPo)
                .refundBatchId(orderPo.getRefundBatchId())
                .channelTradeId(orderPo.getChannelTradeId())
                .build();

        context.setContext(context);
        return context;
    }
}
