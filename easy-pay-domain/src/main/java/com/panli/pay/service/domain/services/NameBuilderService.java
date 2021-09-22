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

package com.panli.pay.service.domain.services;

import com.panli.pay.service.domain.enums.ChannelActionEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_CHANNEL_PREFIX;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_REFUND_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_REFUND_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_ADVANCE_PAYMENT_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAY_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BAR_CODE_REFUND_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BAR_CODE_REFUND_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_CHANNEL_PREFIX;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_REQUEST;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V2_CANCEL_PAYMENT;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_QUERY_CHANNEL;

public class NameBuilderService {

    private static final Map<String, String> ALI_COMMON_CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> WX_COMMON_CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> FZ_SERVICE_MCH_CHANNEL_MAP = new ConcurrentHashMap<>();

    static {
        // TODO: 2021/9/18 register all template name to map

        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliBarCode", ChannelActionEnum.REFUND), ALI_REFUND_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliApp", ChannelActionEnum.REFUND), ALI_REFUND_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliPc", ChannelActionEnum.REFUND), ALI_REFUND_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliWap", ChannelActionEnum.REFUND), ALI_REFUND_CHANNEL);

        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliBarCode", ChannelActionEnum.PAY_QUERY), ALI_PAY_REFUND_QUERY_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliApp", ChannelActionEnum.PAY_QUERY), ALI_PAY_REFUND_QUERY_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliPc", ChannelActionEnum.PAY_QUERY), ALI_PAY_REFUND_QUERY_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliWap", ChannelActionEnum.PAY_QUERY), ALI_PAY_REFUND_QUERY_CHANNEL);

        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliBarCode", ChannelActionEnum.REFUND_QUERY), ALI_PAY_QUERY_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliApp", ChannelActionEnum.REFUND_QUERY), ALI_PAY_QUERY_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliPc", ChannelActionEnum.REFUND_QUERY), ALI_PAY_QUERY_CHANNEL);
        ALI_COMMON_CHANNEL_MAP.put(formatChannelKey("aliWap", ChannelActionEnum.REFUND_QUERY), ALI_PAY_QUERY_CHANNEL);
        // TODO: 2021/7/30  add ali cancel key

        //
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxBarCode", ChannelActionEnum.REFUND), WX_BAR_CODE_REFUND_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxBarCode", ChannelActionEnum.CANCEL), WX_V2_CANCEL_PAYMENT);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapi", ChannelActionEnum.REFUND), WX_V3_REFUND_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiH5", ChannelActionEnum.REFUND), WX_V3_REFUND_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiApp", ChannelActionEnum.REFUND), WX_V3_REFUND_CHANNEL);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchFz", ChannelActionEnum.PROFIT_SHARING_REQUEST), WX_FZ_REQUEST);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchBarCode", ChannelActionEnum.PAY), WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchBarCode", ChannelActionEnum.PAY_QUERY), WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxBarCode", ChannelActionEnum.PAY_QUERY), WX_BARCODE_PAY_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapi", ChannelActionEnum.PAY_QUERY), WX_ADVANCE_PAYMENT_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiH5", ChannelActionEnum.PAY_QUERY), WX_ADVANCE_PAYMENT_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiApp", ChannelActionEnum.PAY_QUERY), WX_ADVANCE_PAYMENT_QUERY_CHANNEL);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxBarCode", ChannelActionEnum.REFUND_QUERY), WX_BAR_CODE_REFUND_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapi", ChannelActionEnum.REFUND_QUERY), WX_V3_REFUND_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiH5", ChannelActionEnum.REFUND_QUERY), WX_V3_REFUND_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiApp", ChannelActionEnum.REFUND_QUERY), WX_V3_REFUND_QUERY_CHANNEL);

        FZ_SERVICE_MCH_CHANNEL_MAP.put("wxServiceMchFz.PAY", "wxServiceMchBarCode");
    }

    public static String getPaymentOrderChannel(String channel, ChannelActionEnum channelActionEnum) {
        String key = String.format("%s.%s", channel, channelActionEnum);
        return FZ_SERVICE_MCH_CHANNEL_MAP.get(key);
    }

    public static String paymentTemplateName(String platformCode, String channelCode) {
        return String.format("%s.%s.%s", platformCode, channelCode, "Pay");
    }

    public static String profitSharingTemplateName(String platformCode, String channelCode) {
        return String.format("%s.%s.%s", platformCode, channelCode, "profitSharing");
    }

    public static String refundTemplateName(String platformCode, String channelCode) {
        return String.format("%s.%s.%s", platformCode, channelCode, "Refund");
    }

    public static String cancelTemplateName(String platformCode, String channelCode) {
        return String.format("%s.%s.%s", platformCode, channelCode, "cancel");
    }

    public static String queryTemplateName(String platformCode, String channelCode) {
        return String.format("%s.%s.%s", platformCode, channelCode, "Query");
    }


    public static String paymentChannelName(String channelCode) {
        return String.format("%s.%s.%s", channelCode, "Channel", "Pay");
    }

    public static String getChannelName(String channelCode, ChannelActionEnum actionEnum) {
        if (StringUtils.startsWithIgnoreCase(channelCode, ALI_CHANNEL_PREFIX)) {
            return ALI_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, actionEnum));
        }
        if (StringUtils.startsWithIgnoreCase(channelCode, WX_CHANNEL_PREFIX)) {
            return WX_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, actionEnum));
        }

        throw new IllegalArgumentException("channel code invalid " + channelCode);
    }

    public static String queryChannelName(String channelCode) {
        if (StringUtils.startsWithIgnoreCase(channelCode, ALI_CHANNEL_PREFIX)) {
            return ALI_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.PAY_QUERY));
        }
        if (StringUtils.startsWithIgnoreCase(channelCode, WX_CHANNEL_PREFIX)) {
            return WX_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.PAY_QUERY));
        }
        return String.format("%s.%s.%s", channelCode, "Channel", "Query");
    }

    public static String refundChannelName(String channelCode) {
        if (StringUtils.startsWithIgnoreCase(channelCode, ALI_CHANNEL_PREFIX)) {
            return ALI_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.REFUND));
        }
        if (StringUtils.startsWithIgnoreCase(channelCode, WX_CHANNEL_PREFIX)) {
            return WX_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.REFUND));
        }
        return String.format("%s.%s.%s", channelCode, "Channel", "Refund");
    }

    public static String refundChannelQueryName(String channelCode) {
        if (StringUtils.startsWithIgnoreCase(channelCode, ALI_CHANNEL_PREFIX)) {
            return ALI_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.REFUND_QUERY));
        }
        if (StringUtils.startsWithIgnoreCase(channelCode, WX_CHANNEL_PREFIX)) {
            return WX_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.REFUND_QUERY));
        }
        return String.format("%s.%s.%s", channelCode, "Channel", "Refund.Query");
    }

    public static String cancelChannelName(String channelCode) {
        if (StringUtils.startsWithIgnoreCase(channelCode, ALI_CHANNEL_PREFIX)) {
            return ALI_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.CANCEL));
        }
        if (StringUtils.startsWithIgnoreCase(channelCode, WX_CHANNEL_PREFIX)) {
            return WX_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, ChannelActionEnum.CANCEL));
        }
        return String.format("%s.%s.%s", channelCode, "Channel", "Cancel");
    }

    public static String formatChannelKey(String prefix, ChannelActionEnum actionEnum) {
        return String.format("%s.%s", prefix, actionEnum.name());
    }
}
