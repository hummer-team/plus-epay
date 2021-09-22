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
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_QUERY_CHANNEL;


/**
 * @author lee
 * @see <a href='https://opendocs.alipay.com/open/194/106039'>线下交易查询</a>
 */
@Service(ALI_PAY_QUERY_CHANNEL)
@Slf4j
public class AliPaymentQuery extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradeQueryRequest>
                , AlipayTradeQueryResponse
                , PaymentContext
                , PaymentQueryResultContext> {

    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradeQueryRequest> builder(BaseContext<PaymentContext> context)
            throws Throwable {
        AliPaymentChannelReqContext<AlipayTradeQueryRequest> reqContext = new AliPaymentChannelReqContext<>();

        reqContext.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));
        String paramter = String.format("{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"trade_no\":\"%s\"}"
                , context.getContext().getTradeId()
                , context.getContext().getChannelTradeId());
        AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
        queryRequest.setBizContent(paramter);
        reqContext.setRequestBody(paramter);
        reqContext.setAliPayRequest(queryRequest);
        //reqContext.setData(reqContext);
        return reqContext;
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
    public AlipayTradeQueryResponse doCall(BaseContext<PaymentContext> context
            , AliPaymentChannelReqContext<AlipayTradeQueryRequest> reqContext) throws Throwable {
        return reqContext.getAlipayClient().execute(reqContext.getAliPayRequest());
    }

    @Override
    public PaymentQueryResultContext parseResp(AlipayTradeQueryResponse resp) {
        return PaymentQueryResultContext.builder()
                .success(resp.isSuccess())
                .tradeId(resp.getOutTradeNo())
                .channelTradeId(resp.getTradeNo())
                .status(resp.getCode())
                .describe(resp.getTradeStatus())
                .channelOriginResponse(JSON.toJSONString(resp))
                .build();
    }
}
