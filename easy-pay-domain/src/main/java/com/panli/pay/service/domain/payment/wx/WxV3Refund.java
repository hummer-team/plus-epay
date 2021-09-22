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
import com.hummer.common.http.HttpResult;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.context.RefundResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxV3RefundReqContext;
import com.panli.pay.service.domain.payment.wx.context.req.WxV3RefundReq;
import com.panli.pay.service.domain.payment.wx.context.resp.WxV3RefundResp;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_CHANNEL;

@Service(WX_V3_REFUND_CHANNEL)
public class WxV3Refund extends BaseV3Payment implements PaymentChannel<WxV3RefundReqContext
        , HttpResult, RefundContext, RefundResultContext> {
    @Autowired
    private ApiSign apiV3Sign;
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxV3RefundReqContext builder(BaseContext<RefundContext> context)
            throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        WxV3RefundReqContext req = new WxV3RefundReqContext();
        req.setMchid(configPo.getMerchantId());

        WxV3RefundReq body = new WxV3RefundReq();
        body.setTransactionId(context.getContext().getChannelTradeId());
        body.setOutTradeNo(context.getContext().getTradeId());
        body.setOutRefundNo(context.getContext().getRefundBatchId());
        body.setNotifyUrl(JSON.parseObject(configPo.getExtendParameter()).getString("notifyUrl"));
        body.setReason(context.getContext().getReason());

        WxV3RefundReq.Amount amount = new WxV3RefundReq.Amount();
        amount.setTotal(convertAmount(context.getContext().getOriginOrderAmount()));
        amount.setRefund(convertAmount(context.getContext().getAmount()));
        amount.setCurrency("CNY");

        body.setAmount(amount);

        req.setReq(body);
        //req.setData(req);
        return req;
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
    public HttpResult doCall(BaseContext<RefundContext> context
            , WxV3RefundReqContext reqContext) throws Throwable {
        String body = JSON.toJSONString(reqContext.getReq());
        return WxPayClient.doPostWith(reqContext.getServiceUrl()
                , body
                , ContentType.APPLICATION_JSON.withCharset("UTF-8").toString()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getMchid(), "POST"
                 , reqContext.getServiceUrl(), body)
                , new BasicHeader("Accept", "application/json"));
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(HttpResult resp) throws Throwable {
        WxV3RefundResp refundResp = JSON.parseObject(resp.getResult(), WxV3RefundResp.class);
        return RefundResultContext.builder()
                .channelRefundId(refundResp.getRefundId())
                .channelOriginResponse(resp.getResult())
                .build();
    }
}
