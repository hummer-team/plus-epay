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

package com.panli.pay.service.domain.payment.ali;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.google.common.collect.Maps;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import com.panli.pay.service.domain.payment.ali.context.resp.AliPaymentPcResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_PC_CHANNEL;

/**
 * @author lee
 * @see <a href='https://opendocs.alipay.com/open/270/105899'>PC支付</a>
 */
@Service(ALI_PAY_PC_CHANNEL)
@Slf4j
public class AliPaymentPc extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradePagePayRequest>, AlipayTradePagePayResponse
                , PaymentContext, PaymentResultContext> {

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradePagePayRequest> builder(BaseContext<PaymentContext> context) throws Throwable {
        AliPaymentChannelReqContext<AlipayTradePagePayRequest> body = new AliPaymentChannelReqContext<>();
        body.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));

        AlipayTradePagePayRequest pagePay = new AlipayTradePagePayRequest();
        pagePay.setNotifyUrl(context.getContext().getChannelConfigPo().getExtParameterVal(NOTIFY_URL_KEY));
        pagePay.setReturnUrl(context.getContext().getChannelConfigPo()
                .getExtParameterValWithAssertNotNull(RETURN_URL_KEY));

        Map<String, Object> map = Maps.newHashMap();
        map.put("out_trade_no", context.getContext().getTradeId());
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("subject", context.getContext().getRemark());
        map.put("total_amount", context.getContext().getAmount());

        pagePay.setBizContent(JSON.toJSONString(map));

        body.setAliPayRequest(pagePay);
        body.setRequestBody(pagePay.getBizContent());

        return body;
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
    public AlipayTradePagePayResponse doCall(BaseContext<PaymentContext> context, AliPaymentChannelReqContext<AlipayTradePagePayRequest> reqContext) throws Throwable {
        return reqContext.getAlipayClient()
                .pageExecute(reqContext.getAliPayRequest());
    }

    /**
     * parse channel response to local context
     *
     * @param resp channel response body
     * @return {@link BaseResultContext}
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(AlipayTradePagePayResponse resp) throws Throwable {
        log.debug("ali pay for pc response is :{}", resp);
        PaymentResultContext context = PaymentResultContext.builder()
                .success(resp.isSuccess())
                .paymentStatus(resp.isSuccess() ? PaymentStatusEnum.WAIT_PAYMENT : PaymentStatusEnum.PAYMENT_FAILED)
                .channelOriginResponse(JSON.toJSONString(resp))
                .build();

        context.setResult(context);
        return context;
    }

    /**
     * builder response message
     *
     * @param result      {@link BaseResultContext}
     * @param channelResp channel response message
     * @return response body is map
     */
    @Override
    public BasePaymentResp<AliPaymentPcResp> builderRespMessage(BaseResultContext<PaymentResultContext> result, AlipayTradePagePayResponse channelResp) {
        result.assertSuccess(channelResp.getMsg());
        AliPaymentPcResp respDto = new AliPaymentPcResp();
        Optional.ofNullable(result.getResult()).ifPresent(r -> {
            respDto.setPaySign(channelResp.getBody());
            respDto.setTradeId(result.getResult().getTradeId());
        });
        return respDto;
    }
}
