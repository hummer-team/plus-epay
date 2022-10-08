package com.panli.pay.service.domain.payment.wx;

import com.hummer.common.http.bo.HttpCertBo;
import com.hummer.common.utils.BigDecimalUtil;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodeRefundReq;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        Map<String, Object> treeMap = composeParams(context);
        treeMap.put("sign", WxApiV2Sign.generateSignatureByMd5(treeMap, configPo.getPrivateKey()));

        WxBarCodeRefundReq reqContext = new WxBarCodeRefundReq();
        reqContext.setXmlBody(WxApiV2Sign.mapToXml(treeMap));
        reqContext.setServiceUrl(configPo.getServiceUrl());
        reqContext.setTimeoutMillis(configPo.getConnectTimeoutMs());
        reqContext.setMerchantId(configPo.getMerchantId());
        reqContext.setCertPath(configPo.getExtParameterValWithAssertNotNull("certPath"));
        reqContext.setCertName(configPo.getExtParameterValWithAssertNotNull("certName"));
        reqContext.setCertPassword(configPo.getExtParameterValWithAssertNotNull("certPassword"));
        reqContext.setCertKsType(configPo.getExtParameterValWithAssertNotNull("certKsType"));
        //reqContext.setData(reqContext);

        return reqContext;
    }

    public Map<String, Object> composeParams(BaseContext<RefundContext> context) {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appid", configPo.getAppId());
        treeMap.put("mch_id", configPo.getMerchantId());
        treeMap.put("nonce_str", WxApiV2Sign.generateNonceStr());
        treeMap.put("out_trade_no", context.getContext().getTradeId());
        treeMap.put("out_refund_no", context.getContext().getRefundBatchId());
        treeMap.put("total_fee", BigDecimalUtil.mulOf2HalfDown(context.getContext().getOriginOrderAmount(),
                BigDecimal.valueOf(100)).intValue());
        treeMap.put("refund_fee", BigDecimalUtil.mulOf2HalfDown(context.getContext().getAmount(),
                BigDecimal.valueOf(100)).intValue());
        String notifyUrl = getNotifyUrl(configPo.getExtendParameter());
        if (StringUtils.isNotEmpty(notifyUrl)) {
            treeMap.put("notify_url", notifyUrl);
        }
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
    public String doCall(BaseContext<RefundContext> context
            , WxBarCodeRefundReq reqContext) throws Throwable {
        HttpCertBo cert = HttpCertBo.builder().password(reqContext.getCertPassword())
                .ksType(reqContext.getCertKsType())
                .certName(reqContext.getCertName())
                .certPath(reqContext.getCertPath())
                .build();
        return WxPayClient.doV2Post(reqContext.getServiceUrl()
                , reqContext.getXmlBody()
                , reqContext.getMerchantId()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , cert);
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
                .success(successOfOption(resultMap))
                .channelRespCode(String.valueOf(resultMap.get("return_code")))
                .channelRespMessage(String.valueOf(resultMap.get("return_msg")))
                .channelSubCode(String.valueOf(resultMap.get("result_code")))
                .channelSubMessage(String.valueOf(resultMap.get("err_code")))
                .channelOriginResponse(resp)
                .channelRefundId(String.valueOf(resultMap.get("refund_id")))
                .build();

        channelResult.setResult(channelResult);
        return channelResult;
    }


    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {
        return null;
    }
}
