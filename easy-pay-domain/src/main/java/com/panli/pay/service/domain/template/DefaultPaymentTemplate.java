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
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.core.AbstractPaymentTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.ConstantDefine;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;

/**
 * the common payment template,include:
 * <p>
 * <ui>
 * <li>ali basic pay</li>
 * <li>weixin basic pay</li>
 * <li>weix service merchant pay</li>
 * </ui>
 * </p>
 */
@Service(ConstantDefine.DEFAULT_PAYMENT_TEMPLATE)
public class DefaultPaymentTemplate extends AbstractPaymentTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;
    @Autowired
    private PaymentOrderDao orderDao;

    /**
     * Generally, it does not need to be Override
     *
     * @param resultContext
     * @param paymentContext
     */
    @Override
    protected void savePaymentResult(PaymentResultContext resultContext, PaymentContext paymentContext
            , BasePaymentChannelReqBodyContext reqBody) {
        //only pay success then create order
        boolean createOrder = resultContext.getPaymentStatus() == PaymentStatusEnum.PAYMENT_SUCCESS;
        resultInfoService.savePaymentResult(resultContext
                , paymentContext
                , reqBody.requestBody()
                , paymentContext.getBeanNumber() != null
                , createOrder);
    }

    @Override
    protected void setContextAndSelfCheck(PaymentContext context) {

        ChannelConfigPo channelConfigPo = checkChannelIsValid(context.getPlatformCode(), context.getChannelCode()
                , ChannelActionEnum.PAY);
        checkIsRepeatedlyPay(context);

        context.setChannelConfigPo(channelConfigPo);
        context.setRequestId(MDC.get(REQUEST_ID));
        context.setPayChannelType(PayChannelTypeEnum.getByChannelCode(context.getChannelCode()));
    }

    /**
     * check payment is exists risk
     *
     * @param paymentContext
     */
    @Override
    protected void checkRisk(PaymentContext paymentContext) {
        //todo
    }

    private void checkIsRepeatedlyPay(PaymentContext context) {
        PaymentOrderPo orderPo = orderDao.queryByTradeIdByCode(context.getTradeId()
                , context.getPlatformCode()
                , context.getChannelCode());
        if (orderPo != null) {
            throw new AppException(40010
                    , String.format("please don't pay repeatedly,%s - %s - %s - %s", context.getPlatformCode()
                    , context.getOrderTag(), context.getChannelCode(), context.getTradeId()));
        }
    }
}
