package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxRefundV3ReqDto;
import com.panli.pay.service.domain.payment.wx.context.resp.WxV3RefundResp;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_CHANNEL;

@Service(WX_V3_REFUND_CHANNEL)
public class WxV3Refund extends BaseV3Payment implements PaymentChannel<WxRefundV3ReqDto
        , String, RefundContext, RefundResultContext> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxRefundV3ReqDto builder(BaseContext<RefundContext> context) {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        WxRefundV3ReqDto req = new WxRefundV3ReqDto();
        req.setServiceUrl(configPo.getServiceUrl());
        req.setTimeoutMillis(configPo.getConnectTimeoutMs());
        req.setTransactionId(context.getContext().getChannelTradeId());
        req.setOutTradeNo(context.getContext().getTradeId());
        req.setOutRefundNo(context.getContext().getRefundBatchId());
        req.setReason(context.getContext().getReason());

        WxRefundV3ReqDto.Amount amount = new WxRefundV3ReqDto.Amount();
        amount.setTotal(convertAmount(context.getContext().getOriginOrderAmount()));
        amount.setRefund(convertAmount(context.getContext().getAmount()));
        amount.setCurrency("CNY");
        req.setAmount(amount);
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
    public String doCall(BaseContext<RefundContext> context
            , WxRefundV3ReqDto reqContext) throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        String body = reqContext.requestBody();
        return WxPayClient.doV3Post(reqContext.getServiceUrl()
                , body
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(configPo.getMerchantId(), HttpMethod.POST.name(),
                        reqContext.getServiceUrl(), body, configPo.getChannelCertPo())
        );
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(String resp) throws Throwable {
        WxV3RefundResp refundResp = JSON.parseObject(resp, WxV3RefundResp.class);
        RefundResultContext result = RefundResultContext.builder()
                .success(convertResult(refundResp))
                .channelRespCode(refundResp.getStatus())
                .channelRefundId(refundResp.getRefundId())
                .channelOriginResponse(resp)
                .build();
        result.setResult(result);
        return result;
    }

    private boolean convertResult(WxV3RefundResp resp) {
        String status = resp.getStatus();
        return "SUCCESS".equals(status) || "PROCESSING".equals(status);
    }
}
