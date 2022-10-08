package com.panli.pay.service.domain.lookup;

import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.AppBusinessAssert;
import com.panli.pay.service.domain.core.AbstractCancelPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractChannelTemplate;
import com.panli.pay.service.domain.core.AbstractNotifyHandlerTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentQueryTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractProfitSharingAddReceiverTemplate;
import com.panli.pay.service.domain.core.AbstractProfitSharingTemplate;
import com.panli.pay.service.domain.core.AbstractRefundTemplate;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.TemplateEnum;
import com.panli.pay.service.domain.services.NameBuilderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_CANCEL_TEMPLATE;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PAYMENT_TEMPLATE;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_ADD_RECEIVER_TEMPLATE;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_QUERY_TEMPLATE;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_REFUND_TEMPLATE;

@Component
@Slf4j
public class LookupService {
    @Autowired
    private Map<String, AbstractPaymentTemplate> paymentTemplateMap;
    @Autowired
    private Map<String, AbstractProfitSharingTemplate> profitSharingTemplateMap;
    @Autowired
    private Map<String, AbstractPaymentQueryTemplate> queryTemplateMap;
    @Autowired
    @SuppressWarnings({"rawtypes"})
    private Map<String, PaymentChannel> paymentChannelMap;
    @Autowired
    private Map<String, AbstractRefundTemplate> refundTemplateMap;
    @Autowired
    private Map<String, AbstractCancelPaymentTemplate> cancelPaymentTemplateMap;
    @Autowired
    @SuppressWarnings({"rawtypes"})
    private Map<String, AbstractNotifyHandlerTemplate> notifyHandlerTemplateMap;
    @Autowired
    private Map<String, AbstractProfitSharingAddReceiverTemplate> profitSharingAddReceiverTemplateMap;
    @Autowired
    private Map<String, AbstractChannelTemplate> channelTemplateMap;

    @SuppressWarnings("unchecked")
    public <T> T lookupTemplate(@NotNull String platformCode
            , @NotNull String channelCode
            , @NotNull TemplateEnum templateEnum) {
        T t = null;
        switch (templateEnum) {
            case PAY:
                t = (T) getOrDefault(paymentTemplateMap, NameBuilderService.paymentTemplateName(platformCode
                        , channelCode), DEFAULT_PAYMENT_TEMPLATE);
                break;
            case QUERY:
                t = (T) getOrDefault(queryTemplateMap, NameBuilderService.queryTemplateName(platformCode
                        , channelCode), DEFAULT_QUERY_TEMPLATE);
                break;
            case REFUND:
                t = (T) getOrDefault(refundTemplateMap, NameBuilderService.refundTemplateName(platformCode
                        , channelCode), DEFAULT_REFUND_TEMPLATE);
                break;
            case CANCEL:
                t = (T) getOrDefault(cancelPaymentTemplateMap, NameBuilderService.cancelTemplateName(platformCode
                        , channelCode), DEFAULT_CANCEL_TEMPLATE);
                break;
            case PROFIT_SHARING_REQUEST:
                t = (T) getOrDefault(profitSharingTemplateMap, NameBuilderService.profitSharingTemplateName(platformCode
                        , channelCode), DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE);
                break;
            case ADD_RECEIVER:
                t = (T) getOrDefault(profitSharingAddReceiverTemplateMap
                        , NameBuilderService.getChannelTemplateName(platformCode, channelCode, templateEnum)
                        , DEFAULT_PROFIT_SHARING_ADD_RECEIVER_TEMPLATE);
                break;
            default:
                t = (T) getOrDefault(channelTemplateMap
                        , NameBuilderService.getChannelTemplateName(platformCode, channelCode, templateEnum)
                        , NameBuilderService.formatTemplateDefaultKey("DEFAULT", templateEnum, "TEMPLATE"));
        }
        AppBusinessAssert.isTrue(t != null, 40004, "template code invalid " + templateEnum);
        return t;
    }

    /**
     * find channel , if not fund then throw {@link AppException}
     *
     * @param channelCode       channelCode
     * @param channelActionEnum channelActionEnum
     * @return {@link PaymentChannel}
     */
    @SuppressWarnings({"rawtypes"})
    public PaymentChannel lookupChannel(
            @NotNull String channelCode
            , @NotNull ChannelActionEnum channelActionEnum) {
        return getAndAssert(paymentChannelMap, NameBuilderService.getChannelName(channelCode, channelActionEnum));
    }

    public <T> T getAndAssert(Map<String, T> map, String key) {
        T r = map.get(key);
        if (r == null) {
            throw new AppException(40004, key + " not fund channel");
        }
        return r;
    }

    public <T> T getOrDefault(Map<String, T> map, String key, String defaultKey) {
        T r = map.get(key);
        if (r == null) {
            return map.get(defaultKey);
        }
        return r;
    }

    public <T> AbstractNotifyHandlerTemplate<T> lookupNotifyHandlerTemplate(String name) {
        return notifyHandlerTemplateMap.<T>get(name);
    }
}
