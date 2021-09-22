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

import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.context.RefundResultContext;
import com.panli.pay.service.domain.core.AbstractRefundTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_REFUND_TEMPLATE;

@Service(YUG_BEAN_REFUND_TEMPLATE)
@Slf4j
public class YugBeanRefundTemplate extends AbstractRefundTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;

    /**
     * check it refund Does it exist risk
     *
     * @param refundContext refund context data
     */
    @Override
    protected void checkRisk(RefundContext refundContext) {
        //todo
    }

    /**
     * save to database
     *
     * @param context
     * @param refundContext
     * @param reqBodyContext
     */
    @Override
    protected void saveRefundResult(BaseResultContext<RefundResultContext> context
            , RefundContext refundContext, BasePaymentChannelReqBodyContext reqBodyContext) {
        resultInfoService.saveRefundResultOfYugBean(context.getResult(),refundContext);
    }

    @Override
    protected ChannelConfigPo checkChannelIsValid(String platformCode, String channelCode, ChannelActionEnum typeEnum) {
        //nothing
        return null;
    }
}
