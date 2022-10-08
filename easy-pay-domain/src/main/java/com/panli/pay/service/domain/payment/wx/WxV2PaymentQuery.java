package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.integration.wxpayment.WXPayConstant;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodePaymentQueryReq;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAY_QUERY_CHANNEL;

/**
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2'>barcode payment query</a>
 */
@Slf4j
@Service(WX_BARCODE_PAY_QUERY_CHANNEL)
public class WxV2PaymentQuery extends BaseWxV2Payment implements PaymentChannel<WxBarCodePaymentQueryReq
        , String, PaymentQueryContext, PaymentQueryResultContext> {
    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public WxBarCodePaymentQueryReq builder(BaseContext<PaymentQueryContext> context)
            throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        Map<String, Object> treeMap = putRequestBody(context, configPo);

        WxBarCodePaymentQueryReq reqContext = new WxBarCodePaymentQueryReq();
        reqContext.setParameter(treeMap);
        reqContext.setServiceUrl(configPo.getServiceUrl());
        reqContext.setTimeoutMillis(configPo.getConnectTimeoutMs());
        //reqContext.setData(reqContext);
        reqContext.setRetry(1);
        reqContext.setMerchantId(configPo.getMerchantId());
        reqContext.setXmlBody(WxApiV2Sign.mapToXml(treeMap));

        return reqContext;
    }

    protected Map<String, Object> putRequestBody(BaseContext<PaymentQueryContext> context, ChannelConfigPo configPo)
            throws Exception {
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appid", configPo.getAppId());
        treeMap.put("mch_id", configPo.getMerchantId());
        treeMap.put("out_trade_no", context.getContext().getTradeId());
        treeMap.put("nonce_str", WxApiV2Sign.generateNonceStr());
        treeMap.put("sign_type", WXPayConstant.MD5);
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
    public String doCall(BaseContext<PaymentQueryContext> context
            , WxBarCodePaymentQueryReq reqContext) throws Throwable {
        return WxPayClient.doV2Post(reqContext.getServiceUrl(), reqContext.getXmlBody()
                , reqContext.getMerchantId(), reqContext.getTimeoutMillis(), reqContext.getRetry());
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(String resp) throws Throwable {
        Map<String, Object> resultMap = WxApiV2Sign.xmlToMap(resp);
        PaymentQueryResultContext channelResult = PaymentQueryResultContext.<PaymentQueryResultContext>builder()
                .success(successOfOption(resultMap))
                .channelRespCode(String.valueOf(resultMap.get("return_code")))
                .channelRespMessage(String.valueOf(resultMap.get("return_msg")))
                .channelSubCode(String.valueOf(resultMap.get("result_code")))
                .channelSubMessage(String.valueOf(resultMap.get("err_code")))
                .status(parsePaymentStatus(resultMap))
                .channelOriginResponse(resp.replaceAll("[\r|\n]", ""))
                .channelTradeId((String) resultMap.get("transaction_id"))
                .build();

        channelResult.setResult(channelResult);
        return channelResult;
    }


    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {

        // TODO status
        return PaymentStatusEnum.PAYMENT_SUCCESS;
    }
}
