package com.panli.pay.service.domain.payment.wx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hummer.common.security.Aes;
import com.hummer.common.utils.BigDecimalUtil;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentReq;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentReqContext;
import com.panli.pay.service.domain.payment.wx.context.resp.WxV3AdvancePaymentResp;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_ADVANCE_PAYMENT_CHANNEL;

/**
 * create weixin payment order, status is wait pay
 *
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml'>预支付统一下单</a>
 */
@Service(WX_ADVANCE_PAYMENT_CHANNEL)
@Slf4j
public class WxV3AdvancePayment extends BaseV3Payment implements PaymentChannel<WxAdvancePaymentReqContext
        , String, PaymentContext, PaymentResultContext> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxAdvancePaymentReqContext builder(BaseContext<PaymentContext> context)
            throws Throwable {
        String openId = String.valueOf(context.getContext().getAffixData().get("openId"));
        ChannelConfigPo configPo = context.getChannelConfigPo();
        WxAdvancePaymentReqContext reqContext = new WxAdvancePaymentReqContext();

        WxAdvancePaymentReq body = new WxAdvancePaymentReq();
        body.setAppid(configPo.getAppId());
        body.setMchid(configPo.getMerchantId());
        body.setOutTradeNo(context.getContext().getTradeId());
        body.setDescription(context.getContext().getRemark());

        JSONObject jsonObject = JSON.parseObject(configPo.getExtendParameter());
        body.setNotifyUrl(jsonObject.getString("notifyUrl"));

        WxAdvancePaymentReq.Payer payer = new WxAdvancePaymentReq.Payer();
        payer.setOpenid(openId);
        body.setPayer(payer);

        WxAdvancePaymentReq.Amount amount = new WxAdvancePaymentReq.Amount();
        amount.setTotal(context.getContext().getAmount().multiply(BigDecimalUtil.valueOf("100")).intValue());
        body.setAmount(amount);

        body.setAttach(Aes.encryptToStringByDefaultKeyIv(context.getRequestId()));

        reqContext.setServiceUrl(configPo.getServiceUrl());
        reqContext.setTimeoutMillis(configPo.getConnectTimeoutMs());
        reqContext.setBody(body);
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
    public String doCall(BaseContext<PaymentContext> context,
                         WxAdvancePaymentReqContext reqContext)
            throws Throwable {
        String body = JSON.toJSONString(reqContext.getBody());
        return WxPayClient.doV3Post(reqContext.getServiceUrl()
                , body
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getBody().getMchid(), "POST"
                        , reqContext.getServiceUrl(), body, context.getChannelConfigPo().getChannelCertPo()));
    }


    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(String resp) throws Throwable {
        Map<String, Object> map = JSON.parseObject(resp);

        PaymentResultContext context = PaymentResultContext.builder()
                .channelAdvancePaymentId(String.valueOf(map.get("prepay_id")))
                .success(true)
                .paymentStatus(PaymentStatusEnum.WAIT_PAYMENT)
                .channelCode("200")
                .channelRespMessage(null)
                .channelOriginResponse(resp)
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
    public BasePaymentResp<WxV3AdvancePaymentResp> builderRespMessage(BaseResultContext<PaymentResultContext> result
            , String channelResp) {
        result.assertSuccess(result.getChannelRespMessage());
        WxV3AdvancePaymentResp resp = new WxV3AdvancePaymentResp();
        Optional.ofNullable(result.getResult()).ifPresent(r -> {
            resp.setChannelAdvancePaymentId(r.getResult().getChannelAdvancePaymentId());
            resp.setChannelCode(r.getResult().getTradeId());
            resp.setChannelTradeId(r.getResult().getChannelTradeId());
            resp.setThreadId(r.getResult().getTradeId());
        });
        return resp;
    }

    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {
        return null;
    }

    @Override
    public boolean successOfOption(Map<String, Object> resultMap) {
        return false;
    }
}
