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

import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryResultContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.context.RefundContext;

/**
 * payment channel api
 *
 * @param <T> pay channel request body context
 * @param <R> channel response
 * @param <C> channel context
 * @param <F> F class:
 *            <p>
 *               <ul>
 *            <li> {@link PaymentResultContext}</li>
 *            <li>{@link RefundContext}</li>
 *            <li> {@link PaymentQueryResultContext}</li>
 *            </ul>
 *            </p>
 * @author lee
 */
public interface PaymentChannel<T extends BasePaymentChannelReqBodyContext
        , R
        , C extends BaseContext<C>
        , F extends BaseResultContext<F>> {
    String NOTIFY_URL_KEY = "notifyUrl";
    String RETURN_URL_KEY = "returnUrl";

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    T builder(BaseContext<C> context) throws Throwable;

    /**
     * call channel service
     *
     * @param context    context
     * @param reqContext reqContext
     * @return
     * @throws Throwable
     */
    R doCall(BaseContext<C> context, T reqContext) throws Throwable;

    /**
     * parse channel response to local context
     *
     * @param resp resp channel response raw content
     * @return {@link com.panli.pay.service.domain.context.BaseResultContext}
     */
    BaseResultContext<F> parseResp(R resp) throws Throwable;

    /**
     * builder response message
     *
     * @param result      {@link BaseResultContext}
     * @param channelResp channel response message
     * @return response body is map
     */
    @SuppressWarnings("unchecked")
    default BasePaymentResp<? extends BasePaymentResp<?>> builderRespMessage(BaseResultContext<F> result, R channelResp) {
        return BasePaymentResp.EMPTY;
    }
}
