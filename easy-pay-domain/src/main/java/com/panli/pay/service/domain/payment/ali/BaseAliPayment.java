/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.panli.pay.service.domain.payment.ali;

import com.alipay.api.DefaultAlipayClient;
import com.panli.pay.service.domain.context.PaymentResultContext;
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
}
