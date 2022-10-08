package com.panli.pay.service.domain.template;

import com.hummer.common.SysConstant;
import com.hummer.common.utils.DateUtil;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.PaymentCancelContext;
import com.panli.pay.service.domain.result.PaymentCancelResultContext;
import com.panli.pay.service.domain.core.AbstractCancelPaymentTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.services.NotifyHandlerService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_CANCEL_TEMPLATE;

@Service(DEFAULT_CANCEL_TEMPLATE)
public class DefaultCancelPaymentTemplate extends AbstractCancelPaymentTemplate {
    @Autowired
    private NotifyHandlerService notifyHandlerService;


    @Override
    protected void handleResult(PaymentCancelResultContext resultContext, PaymentCancelContext paymentContext
            , BasePaymentChannelReqBodyContext reqBody) {
        if (resultContext.isSuccess()) {
            notifyHandlerService.changePaymentStatus(paymentContext.getTradeId(), MDC.get(SysConstant.REQUEST_ID)
                    , null, PaymentStatusEnum.PAYMENT_CLOSED.name()
                    , PaymentStatusEnum.PAYMENT_CANCELED.getCode(), DateUtil.now());
        }
    }

    @Override
    protected void setContextAndSelfCheck(PaymentCancelContext context) {

        ChannelConfigPo channelConfigPo = checkChannelIsValid(context.getPlatformCode(), context.getChannelCode()
                , ChannelActionEnum.CANCEL);

        context.setChannelConfigPo(channelConfigPo);
        context.setRequestId(MDC.get(REQUEST_ID));
    }

    @Override
    protected void checkRisk(PaymentCancelContext paymentContext) {

    }
}
