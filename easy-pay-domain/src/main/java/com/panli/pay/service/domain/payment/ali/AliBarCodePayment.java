package com.panli.pay.service.domain.payment.ali;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.BigDecimalUtil;
import com.hummer.common.utils.DateUtil;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_BAR_CODE_CHANNEL;

/**
 * @author lee
 * @see <a href='https://opendocs.alipay.com/open/194/106039'>条码支付</a>
 */
@Service(ALI_PAY_BAR_CODE_CHANNEL)
@Slf4j
public class AliBarCodePayment extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradePayRequest>, AlipayTradePayResponse, PaymentContext, PaymentResultContext> {

    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradePayRequest> builder(
            BaseContext<PaymentContext> context) throws Throwable {
        String barCode = String.valueOf(context.getContext().getAffixData().get("barcode"));
        if (Strings.isNullOrEmpty(barCode)) {
            throw new AppException(40004, "barcode can not empty.");
        }

        AliPaymentChannelReqContext<AlipayTradePayRequest> body = new AliPaymentChannelReqContext<>();
        body.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));

        //body
        Map<String, Object> map = Maps.newHashMap();
        map.put("out_trade_no", context.getContext().getTradeId());
        map.put("scene", "bar_code");
        map.put("auth_code", barCode);
        map.put("subject", String.format("愚公坊-%s", context.getContext().getRemark()));
        map.put("total_amount", context.getContext().getAmount());

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizContent(JSONObject.toJSONString(map));

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
    public AlipayTradePayResponse doCall(BaseContext<PaymentContext> context
            , AliPaymentChannelReqContext<AlipayTradePayRequest> reqContext) throws Throwable {
        return reqContext.getAlipayClient()
                .execute(reqContext.getAliPayRequest());
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(AlipayTradePayResponse resp) throws Throwable {
        PaymentResultContext context = PaymentResultContext.builder()
                .tradeId(resp.getOutTradeNo())
                .systemCode(resp.isSuccess() ? 0 : 50000)
                .channelTradeId(resp.getTradeNo())
                .channelRespCode(resp.getCode())
                .success(resp.isSuccess())
                .payAmount(BigDecimalUtil.valueOf(resp.getBuyerPayAmount()))
                .channelRespMessage(resp.getSubMsg())
                .paymentStatus(resp.isSuccess() ? PaymentStatusEnum.PAYMENT_SUCCESS : PaymentStatusEnum.PAYMENT_FAILED)
                .paymentDateTime(DateUtil.now())
                .channelOriginResponse(JSON.toJSONString(resp))
                .build();

        context.setResult(context);

        return context;
    }
}
