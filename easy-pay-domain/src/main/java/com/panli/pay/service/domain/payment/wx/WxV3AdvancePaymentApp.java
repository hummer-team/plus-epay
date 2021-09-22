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

package com.panli.pay.service.domain.payment.wx;


import com.hummer.common.http.HttpResult;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_ADVANCE_PAYMENT_APP_CHANNEL;

/**
 * create weixin payment order, status is wait pay
 *
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml'>预支付统一下单(app)</a>
 */
@Service(WX_ADVANCE_PAYMENT_APP_CHANNEL)
@Slf4j
public class WxV3AdvancePaymentApp extends BaseV3Payment implements PaymentChannel<WxAdvancePaymentReqContext
        , HttpResult, PaymentContext, PaymentResultContext> {
    @Autowired
    private WxV3AdvancePayment wxV3AdvancePayment;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxAdvancePaymentReqContext builder(BaseContext<PaymentContext> context)
            throws Throwable {
        return wxV3AdvancePayment.builder(context);
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
    public HttpResult doCall(BaseContext<PaymentContext> context,
                             WxAdvancePaymentReqContext reqContext)
            throws Throwable {
        return wxV3AdvancePayment.doCall(context, reqContext);
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(HttpResult resp) throws Throwable {
        return wxV3AdvancePayment.parseResp(resp);
    }
}
