package com.panli.pay.service.domain.payment.ali;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.google.common.collect.Maps;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import com.panli.pay.service.domain.payment.ali.context.resp.AliPaymentH5Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_WAP_CHANNEL;

/**
 * @author lee
 * @see <a href='https://opendocs.alipay.com/open/203/105285'>h5支付</a>
 */
@Service(ALI_PAY_WAP_CHANNEL)
@Slf4j
public class AliPaymentH5 extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradeWapPayRequest>, AlipayTradeWapPayResponse
                , PaymentContext, PaymentResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradeWapPayRequest> builder(BaseContext<PaymentContext> context) throws Throwable {
        AliPaymentChannelReqContext<AlipayTradeWapPayRequest> body = new AliPaymentChannelReqContext<>();
        body.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));

        AlipayTradeWapPayRequest wapPayReq = new AlipayTradeWapPayRequest();
        wapPayReq.setNotifyUrl(context.getContext().getChannelConfigPo().getExtParameterVal(NOTIFY_URL_KEY));
        wapPayReq.setReturnUrl(context.getContext().getChannelConfigPo()
                .getExtParameterValWithAssertNotNull(RETURN_URL_KEY));

        Map<String, Object> map = Maps.newHashMap();
        map.put("out_trade_no", context.getContext().getTradeId());
        map.put("product_code", "QUICK_WAP_PAY");
        map.put("subject", context.getContext().getRemark());
        map.put("total_amount", context.getContext().getAmount());

        wapPayReq.setBizContent(JSON.toJSONString(map));

        body.setAliPayRequest(wapPayReq);
        body.setRequestBody(wapPayReq.getBizContent());

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
    public AlipayTradeWapPayResponse doCall(BaseContext<PaymentContext> context
            , AliPaymentChannelReqContext<AlipayTradeWapPayRequest> reqContext) throws Throwable {
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
    public BaseResultContext<PaymentResultContext> parseResp(AlipayTradeWapPayResponse resp) throws Throwable {
        log.debug("ali pay for wap response is :{}", resp);
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
    public BasePaymentResp<AliPaymentH5Resp> builderRespMessage(BaseResultContext<PaymentResultContext> result
            , AlipayTradeWapPayResponse channelResp) {
        result.assertSuccess(channelResp.getMsg());
        AliPaymentH5Resp respDto = new AliPaymentH5Resp();
        Optional.ofNullable(result.getResult()).ifPresent(r -> {
            respDto.setPaySign(channelResp.getBody());
            respDto.setTradeId(result.getResult().getTradeId());
        });
        return respDto;
    }
}
