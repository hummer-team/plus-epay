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

import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.context.RefundResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodeRefundReq;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BAR_CODE_REFUND_CHANNEL;

/**
 * <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4'>付款码支付退款申请</a>
 */
@Service(WX_BAR_CODE_REFUND_CHANNEL)
public class WxV2Refund extends BaseWxV2Payment implements PaymentChannel<WxBarCodeRefundReq
        , String, RefundContext, RefundResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxBarCodeRefundReq builder(BaseContext<RefundContext> context) throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appid", configPo.getAppId());
        treeMap.put("mch_id", configPo.getMerchantId());
        treeMap.put("nonce_str", WxApiV2Sign.generateNonceStr());
        treeMap.put("transaction_id", context.getContext().getChannelTradeId());
        treeMap.put("out_trade_no", context.getContext().getTradeId());
        treeMap.put("out_refund_no", context.getContext().getRefundBatchId());
        treeMap.put("total_fee", context.getContext().getOriginOrderAmount());
        treeMap.put("refund_fee", context.getContext().getAmount());
        treeMap.put("notify_url", getNotifyUrl(configPo.getExtendParameter()));
        treeMap.put("sign", WxApiV2Sign.generateSignatureByMd5(treeMap, configPo.getPrivateKey()));

        WxBarCodeRefundReq reqContext = new WxBarCodeRefundReq();
        reqContext.setXmlBody(WxApiV2Sign.mapToXml(treeMap));
        reqContext.setServiceUrl(configPo.getServiceUrl());
        reqContext.setTimeoutMillis(configPo.getConnectTimeoutMs());
        reqContext.setMerchantId(configPo.getMerchantId());

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
    public String doCall(BaseContext<RefundContext> context
            , WxBarCodeRefundReq reqContext) throws Throwable {
        return WxPayClient.doPost(reqContext.getServiceUrl()
                , reqContext.getXmlBody()
                , reqContext.getMerchantId()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry());
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(String resp) throws Throwable {
        Map<String, Object> resultMap = WxApiV2Sign.xmlToMap(resp);

        RefundResultContext channelResult = RefundResultContext.builder()
                .success(successOfBarCode(resultMap))
                .channelRespCode(String.valueOf(resultMap.get("return_code")))
                .channelRespMessage(String.valueOf(resultMap.get("return_msg")))
                .channelSubCode(String.valueOf(resultMap.get("result_code")))
                .channelSubMessage(String.valueOf(resultMap.get("err_code")))
                .channelOriginResponse(resp)
                .build();

        channelResult.setResult(channelResult);
        return channelResult;
    }

    /**
     * check barcode payment result is success.
     *
     * @param resultMap resultMap
     */
    @Override
    public boolean successOfBarCode(Map<String, Object> resultMap) {
        Object returnCode = resultMap.get("return_code");
        Object resultCode = resultMap.get("result_code");
        return "SUCCESS".equals(resultCode) && "SUCCESS".equals(returnCode);
    }
}
