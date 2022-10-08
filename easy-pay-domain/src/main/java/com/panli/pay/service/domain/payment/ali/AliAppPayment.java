package com.panli.pay.service.domain.payment.ali;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.common.collect.Maps;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import com.panli.pay.service.domain.payment.ali.context.resp.AliAppPaymentResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_APP_CHANNEL;

/**
 * @author lee
 * @see <a href='https://opendocs.alipay.com/open/204/01dcc0'>app支付</a>
 */
@Service(ALI_PAY_APP_CHANNEL)
@Slf4j
public class AliAppPayment extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradeAppPayRequest>, AlipayTradeAppPayResponse
                , PaymentContext, PaymentResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradeAppPayRequest> builder(BaseContext<PaymentContext> context) throws Throwable {
        AliPaymentChannelReqContext<AlipayTradeAppPayRequest> body = new AliPaymentChannelReqContext<>();
        body.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));

        //body
        Map<String, Object> map = Maps.newHashMap();
        map.put("out_trade_no", context.getContext().getTradeId());
        map.put("product_code", "QUICK_MSECURITY_PAY");
        map.put("subject", context.getContext().getRemark());
        map.put("total_amount", context.getContext().getAmount());

        map.put("notify_url", context.getContext().getChannelConfigPo().getExtParameterValWithAssertNotNull(NOTIFY_URL_KEY));
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizContent(JSON.toJSONString(map));

        body.setAliPayRequest(request);
        body.setRequestBody(request.getBizContent());

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
    public AlipayTradeAppPayResponse doCall(BaseContext<PaymentContext> context
            , AliPaymentChannelReqContext<AlipayTradeAppPayRequest> reqContext) throws Throwable {
        return reqContext.getAlipayClient()
                .sdkExecute(reqContext.getAliPayRequest());
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(AlipayTradeAppPayResponse resp) throws Throwable {
        log.debug("ali pay for app response is :{}", resp);
        PaymentResultContext context = PaymentResultContext.builder()
                .channelRespCode(resp.getCode())
                .success(resp.isSuccess())
                .channelRespMessage(resp.getSubMsg())
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
    public BasePaymentResp<AliAppPaymentResp> builderRespMessage(BaseResultContext<PaymentResultContext> result
            , AlipayTradeAppPayResponse channelResp) {
        result.assertSuccess(channelResp.getMsg());
        AliAppPaymentResp respDto = new AliAppPaymentResp();
        Optional.ofNullable(result.getResult()).ifPresent(r -> {
            respDto.setPaySign(channelResp.getBody());
            respDto.setTradeId(result.getResult().getTradeId());
        });
        return respDto;
    }
}
