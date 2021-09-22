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

package com.panli.pay.service.domain.lookup;

import com.hummer.common.exceptions.AppException;
import com.panli.pay.service.domain.core.AbstractCanelPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractNotifyHandlerTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentQueryTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentTemplate;
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
    private Map<String, AbstractCanelPaymentTemplate> cancelPaymentTemplateMap;
    @Autowired
    @SuppressWarnings({"rawtypes"})
    private Map<String, AbstractNotifyHandlerTemplate> notifyHandlerTemplateMap;

    @SuppressWarnings("unchecked")
    public <T> T lookupTemplate(@NotNull String platformCode
            , @NotNull String channelCode
            , @NotNull TemplateEnum templateEnum) {
        switch (templateEnum) {
            case PAY:
                return (T) getOrDefault(paymentTemplateMap, NameBuilderService.paymentTemplateName(platformCode
                        , channelCode), DEFAULT_PAYMENT_TEMPLATE);
            case QUERY:
                return (T) getOrDefault(queryTemplateMap, NameBuilderService.queryTemplateName(platformCode
                        , channelCode), DEFAULT_QUERY_TEMPLATE);
            case REFUND:
                return (T) getOrDefault(refundTemplateMap, NameBuilderService.refundTemplateName(platformCode
                        , channelCode), DEFAULT_REFUND_TEMPLATE);
            case CANCEL:
                return (T) getOrDefault(cancelPaymentTemplateMap, NameBuilderService.cancelTemplateName(platformCode
                        , channelCode), DEFAULT_CANCEL_TEMPLATE);
            case PROFIT_SHARING_REQUEST:
                return (T) getOrDefault(profitSharingTemplateMap, NameBuilderService.profitSharingTemplateName(platformCode
                        , channelCode), DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE);
            default:
                throw new AppException(40004, "channel type invalid " + templateEnum);
        }
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
