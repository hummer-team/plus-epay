package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentQueryReqContext;
import com.panli.pay.service.domain.payment.wx.context.resp.WxPaymentQueryResp;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_ADVANCE_PAYMENT_QUERY_CHANNEL;

/**
 * advance payment status Query
 * <a href='https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_2.shtml'>advance payment status Query </a>
 *
 * @author lee
 */
@Service(WX_ADVANCE_PAYMENT_QUERY_CHANNEL)
public class WxV3AdvancePaymentQuery extends BaseV3Payment implements PaymentChannel<WxAdvancePaymentQueryReqContext
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
    public WxAdvancePaymentQueryReqContext builder(BaseContext<PaymentQueryContext> context) throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();

        WxAdvancePaymentQueryReqContext reqContext = new WxAdvancePaymentQueryReqContext();
        reqContext.setMchId(configPo.getMerchantId());
        reqContext.setTransactionId(context.getContext().getChannelTradeId());

        reqContext.setTimeoutMillis(configPo.getConnectTimeoutMs());
        reqContext.setRetry(1);
        reqContext.setServiceUrl(String.format("%s/%s?mchid=%s"
                , configPo.getServiceUrl()
                , context.getContext().getChannelTradeId()
                , configPo.getMerchantId()));

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
                         WxAdvancePaymentQueryReqContext reqContext) throws Throwable {
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
        WxPaymentQueryResp body = JSON.parseObject(resp, new TypeReference<WxPaymentQueryResp>() {
        });
        PaymentQueryResultContext result =
                PaymentQueryResultContext.builder()
                        // TODO status
                        .status(parsePaymentStatus(null))
                        .success(true)
                        .tradeId(body.getOutTradeNo())
                        .channelTradeId(body.getTransactionId())
                        .statusDesc(body.getTradeStateDesc())
                        .channelOriginResponse(resp)
                        .build();

        result.setResult(result);
        return result;
    }

}
