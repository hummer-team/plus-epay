package com.panli.pay.service.domain.core;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.AppBusinessAssert;
import com.hummer.common.utils.BigDecimalUtil;
import com.hummer.core.SpringApplicationContext;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.event.SysLogEvent;
import com.panli.pay.support.model.po.PaymentOrderPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractRefundTemplate extends AbstractTemplate {
    @Autowired
    private PaymentOrderDao paymentOrderDao;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public BaseResultContext<RefundResultContext> doRefund(RefundContext context, PaymentChannel channel) {
        checkRisk(context);
        setContextAndSelfCheck(context);
        BaseResultContext<RefundResultContext> resultContext;
        try {
            BasePaymentChannelReqBodyContext reqBodyContext = channel.builder(context);
            resultContext = channel.parseResp(channel.doCall(context, reqBodyContext));
            saveRefundResult(resultContext, context, reqBodyContext);
            AppBusinessAssert.isTrue(resultContext.isSuccess(), 500,
                    resultContext.getChannelRespMessage() + resultContext.getChannelSubMessage());
        } catch (Throwable e) {
            log.error("refund fail,{} - {} - {} -"
                    , context.getTradeId(), context.getChannelTradeId(), context.getRefundBatchId(), e);
            //publish sys event
            SpringApplicationContext.getApplicationContext()
                    .publishEvent(new SysLogEvent(AbstractRefundTemplate.class
                            , context.getPlatformCode(), context.getPlatformSubType(), context.getChannelCode()
                            , JSON.toJSONString(context), "refund fail", e));

            throw new AppException(50000, "refund failed " + e.getMessage(), e);
        }
        return resultContext;
    }

    /**
     * check it refund Does it exist risk
     *
     * @param refundContext refund context data
     */
    protected abstract void checkRisk(RefundContext refundContext);

    /**
     * save to database
     *
     * @param context
     * @param refundContext
     * @param reqBodyContext
     */
    protected abstract void saveRefundResult(BaseResultContext<RefundResultContext> context
            , RefundContext refundContext
            , BasePaymentChannelReqBodyContext reqBodyContext);

    /**
     * check order is allow refund , if can not then throw exception
     *
     * @param context
     * @return
     */
    protected PaymentOrderPo checkIsAllowRefund(RefundContext context) {
        PaymentOrderPo flowPo =
                paymentOrderDao.queryOneByTradeIdAndCodeAndUserId(context.getTradeId()
                        , context.getChannelCode()
                        , context.getUserId());
        if (flowPo == null) {
            throw new AppException(40004, "tradeId invalid:" + context.getTradeId());
        }

        if (PaymentStatusEnum.getByCode(flowPo.getStatusCode()) != PaymentStatusEnum.PAYMENT_SUCCESS) {
            throw new AppException(40005, "this order status can not support refund");
        }

        if (BigDecimalUtil.greaterThan(context.getAmount(), flowPo.getAmount())) {
            throw new AppException(40006, "refund amount can not greater than order amount");
        }

        return flowPo;
    }

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    protected void setContextAndSelfCheck(RefundContext context) {
        PaymentOrderPo flowPo = checkIsAllowRefund(context);
        context.setChannelConfigPo(checkChannelIsValid(flowPo.getPlatformCode(), flowPo.getChannelCode()
                , ChannelActionEnum.REFUND));
        //set context
        context.setPlatformCode(flowPo.getPlatformCode());
        context.setOrderTag(flowPo.getOrderTag());
        context.setPlatformSubType(flowPo.getPlatformSubType());
        context.setPaymentOrder(flowPo);
        context.setChannelTradeId(flowPo.getChannelTradeId());
        context.setOriginOrderAmount(flowPo.getAmount());
    }
}
