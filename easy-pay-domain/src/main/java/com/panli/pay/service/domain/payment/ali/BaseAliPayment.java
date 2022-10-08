package com.panli.pay.service.domain.payment.ali;

import com.alipay.api.DefaultAlipayClient;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseAliPayment {

    public DefaultAlipayClient builderAlipayClient(ChannelConfigPo configPo) {
        //ali client
        return DefaultAlipayClient.builder(configPo.getServiceUrl()
                        , configPo.getMerchantId()
                        , configPo.getPrivateKey())
                .connectTimeout(configPo.getConnectTimeoutMs())
                .readTimeout(configPo.getRedTimeoutMs())
                .alipayPublicKey(configPo.getPublicKey())
                .format("json")
                .charset("UTF-8")
                .signType("RSA2")
                .build();
    }

    public Map<String, Object> builderCommonResp(PaymentResultContext context) {
        Map<String, Object> map = new ConcurrentHashMap<>(7);
        if (StringUtils.isNotEmpty(context.getChannelCode())) {
            map.put("channelCode", context.getChannelCode());
        }
        if (StringUtils.isNotEmpty(context.getTradeId())) {
            map.put("tradeId", context.getTradeId());
        }
        return map;
    }

    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {

        // TODO status
        return PaymentStatusEnum.PAYMENT_SUCCESS;
    }
}
