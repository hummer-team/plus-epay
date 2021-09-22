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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hummer.common.http.HttpResult;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.context.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxV3RefundQueryReq;
import com.panli.pay.service.domain.payment.wx.context.resp.WxV3RefundQueryResp;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_QUERY_CHANNEL;

/**
 * <a href='https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_10.shtml'>refund query</a>
 *
 * @author lee
 */
@Service(WX_V3_REFUND_QUERY_CHANNEL)
public class WxV3RefundQuery extends BaseV3Payment implements PaymentChannel<WxV3RefundQueryReq
        , HttpResult, PaymentQueryContext, PaymentQueryResultContext> {

    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxV3RefundQueryReq builder(BaseContext<PaymentQueryContext> context) throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();

        WxV3RefundQueryReq reqContext = new WxV3RefundQueryReq();
        reqContext.setAppId(configPo.getAppId());
        reqContext.setMchId(configPo.getMerchantId());
        reqContext.setServiceUrl(String.format("%s/%s", configPo.getServiceUrl(), context.getContext().getRefundBatchId()));
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
    public HttpResult doCall(BaseContext<PaymentQueryContext> context,
                             WxV3RefundQueryReq reqContext) throws Throwable {
        return WxPayClient.doGet(reqContext.getServiceUrl()
                , ContentType.APPLICATION_JSON.withCharset("UTF-8").toString()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getMchId(), "GET"
                        , reqContext.getServiceUrl(), null)
                , new BasicHeader("Accept", "application/json"));
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(HttpResult resp) throws Throwable {
        WxV3RefundQueryResp resp1 = JSON.parseObject(resp.getResult(), new TypeReference<WxV3RefundQueryResp>() {
        });

        PaymentQueryResultContext result = PaymentQueryResultContext.builder()
                .channelOriginResponse(resp.getResult())
                .status(resp1.getStatus())
                .tradeId(resp1.getOutTradeNo())
                .channelTradeId(resp1.getTransactionId())
                .success(true)
                .build();

        result.setResult(result);
        return result;
    }
}
