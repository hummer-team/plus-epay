package com.panli.pay.service.domain.services;

import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.TemplateEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.panli.pay.service.domain.enums.ChannelActionEnum.PROFIT_SHARING_RATE_QUERY;
import static com.panli.pay.service.domain.enums.ChannelActionEnum.PROFIT_SHARING_RETURN;
import static com.panli.pay.service.domain.enums.ChannelActionEnum.PROFIT_SHARING_UNFREEZE;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_CHANNEL_PREFIX;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_REFUND_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_REFUND_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_ADVANCE_PAYMENT_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CANCEL_SERVICE_MCH;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAY_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BAR_CODE_REFUND_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BAR_CODE_REFUND_QUERY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_CHANNEL_PREFIX;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_ADD_RECEIVER;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_RATE_QUERY;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_REQUEST;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_RETURN;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_UNFREEZE;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_SERVICE_PAYMENT_REFUND;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V2_CANCEL_PAYMENT;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V3_REFUND_QUERY_CHANNEL;

public class NameBuilderService {

    private static final Map<String, String> ALI_COMMON_CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> WX_COMMON_CHANNEL_MAP = new ConcurrentHashMap<>();

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
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchFz", ChannelActionEnum.ADD_RECEIVER), WX_FZ_ADD_RECEIVER);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchFz", PROFIT_SHARING_RATE_QUERY), WX_FZ_RATE_QUERY);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchFz", PROFIT_SHARING_UNFREEZE), WX_FZ_UNFREEZE);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchFz", PROFIT_SHARING_RETURN), WX_FZ_RETURN);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchBarCode", ChannelActionEnum.PAY), WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchBarCode", ChannelActionEnum.PAY_QUERY), WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchBarCode", ChannelActionEnum.CANCEL), WX_BARCODE_PAYMENT_CANCEL_SERVICE_MCH);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxServiceMchBarCode", ChannelActionEnum.REFUND), WX_SERVICE_PAYMENT_REFUND);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxBarCode", ChannelActionEnum.PAY_QUERY), WX_BARCODE_PAY_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapi", ChannelActionEnum.PAY_QUERY), WX_ADVANCE_PAYMENT_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiH5", ChannelActionEnum.PAY_QUERY), WX_ADVANCE_PAYMENT_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiApp", ChannelActionEnum.PAY_QUERY), WX_ADVANCE_PAYMENT_QUERY_CHANNEL);

        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxBarCode", ChannelActionEnum.REFUND_QUERY), WX_BAR_CODE_REFUND_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapi", ChannelActionEnum.REFUND_QUERY), WX_V3_REFUND_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiH5", ChannelActionEnum.REFUND_QUERY), WX_V3_REFUND_QUERY_CHANNEL);
        WX_COMMON_CHANNEL_MAP.put(formatChannelKey("wxJsapiApp", ChannelActionEnum.REFUND_QUERY), WX_V3_REFUND_QUERY_CHANNEL);


    }

    public static String getChannelTemplateName(String platformCode, String channelCode, TemplateEnum template) {
        return String.format("%s.%s.%s", platformCode, channelCode, template.name());
    }

    public static String formatTemplateDefaultKey(String prefix, TemplateEnum template, String suffix) {
        return String.format("%s.%s.%s", prefix, template.name(), suffix);
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

    public static String getChannelName(String channelCode, ChannelActionEnum actionEnum) {
        if (StringUtils.startsWithIgnoreCase(channelCode, ALI_CHANNEL_PREFIX)) {
            return ALI_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, actionEnum));
        }
        if (StringUtils.startsWithIgnoreCase(channelCode, WX_CHANNEL_PREFIX)) {
            return WX_COMMON_CHANNEL_MAP.get(formatChannelKey(channelCode, actionEnum));
        }

        throw new IllegalArgumentException("channel code invalid " + channelCode);
    }


    public static String formatChannelKey(String prefix, ChannelActionEnum actionEnum) {
        return String.format("%s.%s", prefix, actionEnum.name());
    }

}
