package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
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
        , String, PaymentQueryContext, PaymentQueryResultContext> {

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
    public String doCall(BaseContext<PaymentQueryContext> context,
                         WxV3RefundQueryReq reqContext) throws Throwable {
        return WxPayClient.doGet(reqContext.getServiceUrl()
                , ContentType.APPLICATION_JSON.withCharset("UTF-8").toString()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getMchId(), "GET"
                        , reqContext.getServiceUrl(), null, context.getChannelConfigPo().getChannelCertPo())
                , new BasicHeader("Accept", "application/json"));
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(String resp) throws Throwable {
        WxV3RefundQueryResp resp1 = JSON.parseObject(resp, new TypeReference<WxV3RefundQueryResp>() {
        });

        PaymentQueryResultContext result = PaymentQueryResultContext.builder()
                .channelOriginResponse(resp)
                // TODO status
                .status(parsePaymentStatus(null))
                .tradeId(resp1.getOutTradeNo())
                .channelTradeId(resp1.getTransactionId())
                .success(true)
                .build();

        result.setResult(result);
        return result;
    }
}
