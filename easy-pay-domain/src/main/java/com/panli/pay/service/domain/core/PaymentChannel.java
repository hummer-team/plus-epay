package com.panli.pay.service.domain.core;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
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
     * @return {@link BaseResultContext}
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
