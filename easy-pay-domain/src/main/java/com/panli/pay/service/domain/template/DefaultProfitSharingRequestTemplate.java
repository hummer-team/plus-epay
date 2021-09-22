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

import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.core.AbstractProfitSharingTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE;

/**
 * ProfitSharing default implement template
 *
 * @author edz
 */
@Service(DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE)
public class DefaultProfitSharingRequestTemplate extends AbstractProfitSharingTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;
    @Autowired
    private PaymentOrderDao orderDao;

    /**
     * Generally, it does not need to be Override
     *
     * @param resultContext context
     * @param context       payment info
     * @param reqBody       request payment body
     */
    @Override
    protected void savePaymentResult(PaymentResultContext resultContext
            , BaseContext<? extends BaseContext<?>> context, BasePaymentChannelReqBodyContext reqBody) {
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.setTradeId(context.getTradeId());
        paymentContext.setUserId(context.getUserId());
        paymentContext.setPlatformCode(context.getPlatformCode());
        paymentContext.setChannelCode(context.getChannelCode());

        // TODO: 2021/9/18 save Profit-sharing result

        resultInfoService.saveProfitSharingResult(resultContext, paymentContext, reqBody.requestBody());
    }

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    @Override
    protected void setContextAndSelfCheck(BaseContext<? extends BaseContext<?>> context) {

        ChannelConfigPo channelConfigPo = checkChannelIsValid(context.getPlatformCode(), context.getChannelCode()
                , ChannelActionEnum.PAY);
        checkIsAllowProfitSharing(context);

        context.setChannelConfigPo(channelConfigPo);
        context.setRequestId(MDC.get(REQUEST_ID));
    }

    /**
     * check payment is exists risk
     *
     * @param context context
     */
    @Override
    protected void checkRisk(BaseContext<? extends BaseContext<?>> context) {
        // TODO: 2021/9/18 risk check
    }

    private void checkIsAllowProfitSharing(BaseContext<? extends BaseContext<?>> context) {
        // TODO: 2021/9/18 check is allow profit sharing
    }
}
