package com.panli.pay.service.domain.payment.wx;

import com.hummer.common.http.bo.HttpCertBo;
import com.panli.pay.integration.wxpayment.WXPayConstant;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentCancelContext;
import com.panli.pay.service.domain.result.PaymentCancelResultContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodePaymentCancelReq;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V2_CANCEL_PAYMENT;

/**
 * @author chenwei
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2'>barcode payment query</a>
 */
@Slf4j
@Service(WX_V2_CANCEL_PAYMENT)
public class WxV2BarcodePaymentCancel extends BaseWxV2Payment implements PaymentChannel<WxBarCodePaymentCancelReq
        , String, PaymentCancelContext, PaymentCancelResultContext> {


    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public WxBarCodePaymentCancelReq builder(BaseContext<PaymentCancelContext> context)
            throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        Map<String, Object> treeMap = putRequestBody(context, configPo);
        treeMap.put("sign", WxApiV2Sign.generateSignatureByMd5(treeMap, configPo.getPrivateKey()));
        WxBarCodePaymentCancelReq req = new WxBarCodePaymentCancelReq();
        req.setParameter(treeMap);
        req.setServiceUrl(configPo.getServiceUrl());
        req.setTimeoutMillis(configPo.getConnectTimeoutMs());
        req.setRetry(1);
        req.setMerchantId(configPo.getMerchantId());
        req.setXmlBody(WxApiV2Sign.mapToXml(treeMap));
        req.setCertPath(configPo.getExtParameterValWithAssertNotNull("certPath"));
        req.setCertName(configPo.getExtParameterValWithAssertNotNull("certName"));
        req.setCertPassword(configPo.getExtParameterValWithAssertNotNull("certPassword"));
        req.setCertKsType(configPo.getExtParameterValWithAssertNotNull("certKsType"));
        return req;
    }

    protected Map<String, Object> putRequestBody(BaseContext<PaymentCancelContext> context, ChannelConfigPo configPo) {
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appid", configPo.getAppId());
        treeMap.put("mch_id", configPo.getMerchantId());
        treeMap.put("out_trade_no", context.getContext().getTradeId());
        treeMap.put("nonce_str", WxApiV2Sign.generateNonceStr());
        treeMap.put("sign_type", WXPayConstant.MD5);
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
    public String doCall(BaseContext<PaymentCancelContext> context
            , WxBarCodePaymentCancelReq reqContext) throws Throwable {
        HttpCertBo cert = HttpCertBo.builder().password(reqContext.getCertPassword())
                .ksType(reqContext.getCertKsType())
                .certName(reqContext.getCertName())
                .certPath(reqContext.getCertPath())
                .build();
        return WxPayClient.doV2Post(reqContext.getServiceUrl(), reqContext.getXmlBody(), reqContext.getMerchantId()
                , reqContext.getTimeoutMillis(), reqContext.getRetry(), cert);
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentCancelResultContext> parseResp(String resp) throws Throwable {
        Map<String, Object> resultMap = WxApiV2Sign.xmlToMap(resp);
        PaymentCancelResultContext channelResult = PaymentCancelResultContext.<PaymentQueryResultContext>builder()
                .success(successOfOption(resultMap))
                .channelRespCode(String.valueOf(resultMap.get("return_code")))
                .channelRespMessage(String.valueOf(resultMap.get("return_msg")))
                .channelSubCode(String.valueOf(resultMap.get("result_code")))
                .channelSubMessage(String.valueOf(resultMap.get("err_code")))
                .status(String.valueOf(resultMap.get("trade_state")))
                .channelOriginResponse(resp.replaceAll("[\r|\n]", ""))
                .recall(confirmRecall(resultMap))
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
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {
        return null;
    }

    public boolean confirmRecall(Map<String, Object> resultMap) {
        if (successOfOption(resultMap)) {
            return false;
        }
        String errCode = (String) resultMap.get("err_code");
        if (errCode == null) {
            return false;
        }
        switch (errCode) {
            case "SYSTEMERROR":
            case "USERPAYING":
                return true;
            default:
                return false;
        }

    }

}
