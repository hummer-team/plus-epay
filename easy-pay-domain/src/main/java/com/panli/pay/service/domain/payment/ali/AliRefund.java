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
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hummer.common.utils.BigDecimalUtil;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.context.RefundResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_REFUND_CHANNEL;

@Service(ALI_REFUND_CHANNEL)
@Slf4j
public class AliRefund extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradeRefundRequest>, AlipayTradeRefundResponse, RefundContext, RefundResultContext> {
    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradeRefundRequest> builder(BaseContext<RefundContext> context) {
        AliPaymentChannelReqContext<AlipayTradeRefundRequest> reqContext = new AliPaymentChannelReqContext<>();
        reqContext.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        String body = String.format("{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"trade_no\":\"%s\"," +
                        "\"out_request_no\":\"%s\"," +
                        "\"refund_amount\":\"%s\"}"
                , context.getContext().getTradeId()
                , context.getContext().getChannelTradeId()
                , context.getContext().getRefundBatchId()
                , context.getContext().getAmount());
        request.setBizContent(body);
        reqContext.setAliPayRequest(request);
        reqContext.setRequestBody(body);
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
    public AlipayTradeRefundResponse doCall(BaseContext<RefundContext> context,
                                            AliPaymentChannelReqContext<AlipayTradeRefundRequest> reqContext)
            throws Throwable {
        return reqContext.getAlipayClient().execute(reqContext.getAliPayRequest());
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(AlipayTradeRefundResponse resp) {
        RefundResultContext context = RefundResultContext.<RefundResultContext>builder()
                .amount(BigDecimalUtil.valueOf(resp.getRefundFee()))
                .channelOriginResponse(JSON.toJSONString(resp))
                .channelRespCode(resp.getCode())
                .channelSubCode(resp.getSubCode())
                .channelRespMessage(resp.getSubMsg())
                .success(resp.isSuccess())
                .build();

        context.setResult(context);
        return context;
    }
}
