package com.panli.pay.service.domain.payment.wx;

import com.google.common.base.Strings;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.DateUtil;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodePaymentReqContext;
import com.panli.pay.service.domain.payment.wx.context.resp.WxBarCodePaymentReturnResp;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CHANNEL;

/**
 * weixin payment by bar code
 *
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1'>付款码支付</a>
 */
@Service(WX_BARCODE_PAYMENT_CHANNEL)
public class WxV2Payment extends BaseWxV2Payment implements PaymentChannel<WxBarCodePaymentReqContext
        , String, PaymentContext, PaymentResultContext> {

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxBarCodePaymentReqContext builder(BaseContext<PaymentContext> context)
            throws Throwable {
        String barCode = String.valueOf(context.getContext().getAffixData().get("barcode"));
        ParameterAssertUtil.assertConditionFalse(Strings.isNullOrEmpty(barCode)
                , () -> new AppException(40004, "barCode can not empty"));

        ChannelConfigPo configPo = context.getChannelConfigPo();

        Map<String, Object> treeMap = putRequestBody(context, barCode, configPo);

        WxBarCodePaymentReqContext reqContext = new WxBarCodePaymentReqContext();
        reqContext.setXmlBody(WxApiV2Sign.mapToXml(treeMap));
        reqContext.setServiceUrl(configPo.getServiceUrl());
        reqContext.setTimeoutMillis(configPo.getConnectTimeoutMs());
        //reqContext.setData(reqContext);
        reqContext.setMerchantId(configPo.getMerchantId());
        return reqContext;
    }

    protected Map<String, Object> putRequestBody(BaseContext<PaymentContext> context
            , String barCode, ChannelConfigPo configPo) throws Exception {
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("total_fee", context.getContext().getAmount().multiply(BigDecimal.valueOf(100)).intValue());
        treeMap.put("appid", configPo.getAppId());
        treeMap.put("mch_id", configPo.getMerchantId());
        treeMap.put("nonce_str", WxApiV2Sign.generateNonceStr());
        treeMap.put("body", context.getContext().getRemark());
        treeMap.put("out_trade_no", context.getContext().getTradeId());
        treeMap.put("spbill_create_ip", context.getContext().getTerminalIp());
        treeMap.put("auth_code", barCode);
        treeMap.put("sign_type", "MD5");
        treeMap.put("sign", WxApiV2Sign.generateSignatureByMd5(treeMap, configPo.getPrivateKey()));

        return treeMap;
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
    public String doCall(BaseContext<PaymentContext> context
            , WxBarCodePaymentReqContext reqContext) throws Throwable {

        return WxPayClient.doV2Post(reqContext.getServiceUrl()
                , reqContext.getXmlBody()
                , reqContext.getMerchantId()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry());
    }

    /**
     * parse channel response to local context
     *
     * @param resp channel response raw content
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(String resp) throws Throwable {
        Map<String, Object> resultMap = WxApiV2Sign.xmlToMap(resp);
        boolean success = successOfOption(resultMap);
        PaymentResultContext channelResult = PaymentResultContext.builder()
                .success(success)
                .channelRespCode(String.valueOf(resultMap.get("return_code")))
                .channelRespMessage(String.format("%s|%s", resultMap.get("return_msg")
                        , resultMap.getOrDefault("err_code", String.valueOf(resultMap.get("return_code")))))
                .channelSubCode(String.valueOf(resultMap.get("result_code")))
                .channelSubMessage(String.valueOf(resultMap.get("err_code_des")))
                .paymentStatus(parsePaymentStatus(resultMap))
                .channelTradeId((String) resultMap.get("transaction_id"))
                .channelOriginResponse(resp)
                .paymentDateTime(DateUtil.now())
                .build();

        channelResult.setResult(channelResult);
        return channelResult;
    }


    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {
        if (successOfOption(resultMap)) {
            return PaymentStatusEnum.PAYMENT_SUCCESS;
        }
        return PaymentStatusEnum.PAYMENT_FAILED;
    }

    /**
     * builder response message
     *
     * @param result      {@link BaseResultContext}
     * @param channelResp channel response message
     * @return response body is map
     */
    @Override
    public BasePaymentResp<? extends BasePaymentResp<?>> builderRespMessage(BaseResultContext<PaymentResultContext> result
            , String channelResp) {

        WxBarCodePaymentReturnResp resp = new WxBarCodePaymentReturnResp();
        Optional.ofNullable(result.getResult()).ifPresent(r -> {
            resp.setTradeId(r.getTradeId());
            resp.setStatus(r.getPaymentStatus().getCode());
            resp.setStatusDesc(r.getPaymentStatus().getCode2());
            resp.setChannelRespMessage(r.getChannelRespMessage());
            resp.setMsg(r.getChannelSubMessage());
            resp.setChannelTradeId(r.getChannelTradeId());
        });

        return resp;
    }
}
