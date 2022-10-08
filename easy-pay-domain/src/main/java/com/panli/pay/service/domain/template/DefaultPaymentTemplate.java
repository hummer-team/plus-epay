package com.panli.pay.service.domain.template;

import com.hummer.common.exceptions.AppException;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.AbstractPaymentTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.ConstantDefine;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.event.PaymentInProcessEvent;
import com.panli.pay.service.domain.event.bo.PaymentInProcessBo;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;

/**
 * the common payment template,include:
 * <p>
 * <ui>
 * <li>ali basic pay</li>
 * <li>weixin basic pay</li>
 * <li>weix service merchant pay</li>
 * </ui>
 * </p>
 */
@Service(ConstantDefine.DEFAULT_PAYMENT_TEMPLATE)
public class DefaultPaymentTemplate extends AbstractPaymentTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;
    @Autowired
    private PaymentOrderDao orderDao;

    /**
     * Generally, it does not need to be Override
     *
     * @param resultContext
     * @param paymentContext
     */
    @Override
    protected void handleResult(PaymentResultContext resultContext, PaymentContext paymentContext
            , BasePaymentChannelReqBodyContext reqBody) {
        //only pay success then create order
        boolean createOrder = resultContext.getPaymentStatus() == PaymentStatusEnum.PAYMENT_SUCCESS
                || resultContext.getPaymentStatus() == PaymentStatusEnum.WAIT_PAYMENT;
        boolean createRecord = paymentContext.getBeanNumber() == null;
        boolean createPayFlow = resultContext.getPaymentStatus() == PaymentStatusEnum.PAYMENT_SUCCESS;
        resultInfoService.savePaymentResult(resultContext
                , paymentContext, reqBody.requestBody(), createRecord, createOrder, createPayFlow);

        // payment in process event
        if (resultContext.getPaymentStatus() == PaymentStatusEnum.WAIT_PAYMENT
                || resultContext.getPaymentStatus() == PaymentStatusEnum.UNKNOWN) {
            String subAppId = (String) paymentContext.getContext().getAffixData().get("subAppId");
            String subMchId = (String) paymentContext.getContext().getAffixData().get("subMchId");
            PaymentInProcessBo bo = new PaymentInProcessBo();
            bo.setChannelCode(paymentContext.getChannelCode());
            bo.setPaymentOutTime(paymentContext.getPaymentTimeOut());
            bo.setPlatformCode(paymentContext.getPlatformCode());
            bo.setPlatformSubType(paymentContext.getPlatformSubType());
            bo.setSubAppId(subAppId);
            bo.setSubMchId(subMchId);
            bo.setTradeId(paymentContext.getTradeId());
            bo.setUserId(paymentContext.getUserId());
            SpringApplicationContext.publishEvent(new PaymentInProcessEvent(DefaultPaymentTemplate.class, bo));
        }
    }

    @Override
    protected void setContextAndSelfCheck(PaymentContext context) {

        ChannelConfigPo channelConfigPo = checkChannelIsValid(context.getPlatformCode(), context.getChannelCode()
                , ChannelActionEnum.PAY);
        checkIsRepeatedlyPay(context);

        context.setChannelConfigPo(channelConfigPo);
        context.setRequestId(MDC.get(REQUEST_ID));
        context.setPayChannelType(PayChannelTypeEnum.getByChannelCode(context.getChannelCode()));
    }

    /**
     * check payment is exists risk
     *
     * @param paymentContext
     */
    @Override
    protected void checkRisk(PaymentContext paymentContext) {
        //todo
    }

    private void checkIsRepeatedlyPay(PaymentContext context) {
        PaymentOrderPo orderPo = orderDao.queryByTradeIdByCode(context.getTradeId()
                , context.getPlatformCode()
                , context.getChannelCode());
        if (orderPo != null) {
            throw new AppException(40010
                    , String.format("please don't pay repeatedly,%s - %s - %s - %s", context.getPlatformCode()
                    , context.getOrderTag(), context.getChannelCode(), context.getTradeId()));
        }
    }
}
