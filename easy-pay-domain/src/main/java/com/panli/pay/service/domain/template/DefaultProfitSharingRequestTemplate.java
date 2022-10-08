package com.panli.pay.service.domain.template;

import com.hummer.common.utils.AppBusinessAssert;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.result.ProfitSharingResultContext;
import com.panli.pay.service.domain.core.AbstractProfitSharingTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE;

/**
 * ProfitSharing default implement template
 *
 * @author edz
 */
@Service(DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE)
public class DefaultProfitSharingRequestTemplate extends AbstractProfitSharingTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;
    @Autowired
    private PaymentOrderDao paymentOrderDao;

    /**
     * Generally, it does not need to be Override
     *
     * @param resultContext context
     * @param context       payment info
     * @param reqBody       request payment body
     */
    @Override
    protected void savePaymentResult(ProfitSharingResultContext resultContext
            , BaseContext<? extends BaseContext<?>> context, BasePaymentChannelReqBodyContext reqBody) {

        PaymentResultContext result = ObjectCopyUtils.copy(resultContext, PaymentResultContext.class);
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.setTradeId(context.getTradeId());
        paymentContext.setUserId(context.getUserId());
        paymentContext.setPlatformCode(context.getPlatformCode());
        paymentContext.setChannelCode(context.getChannelCode());

        //save Profit-sharing result
        resultInfoService.saveProfitSharingResult(result, paymentContext, reqBody.requestBody());
    }

    /**
     * implement payment verify it before
     *
     * @param context context
     */
    @Override
    protected void setContextAndSelfCheck(BaseContext<? extends BaseContext<?>> context) {

        ChannelConfigPo channelConfigPo = checkChannelIsValid(context.getPlatformCode(), context.getChannelCode()
                , ChannelActionEnum.PAY);
        checkIsAllowProfitSharing(context);

        context.setChannelConfigPo(channelConfigPo);
        context.setRequestId(MDC.get(REQUEST_ID));

        PaymentOrderPo orderPo = paymentOrderDao.queryOneByTradeIdAndCode(context.getPlatformCode(), context.getTradeId());
        AppBusinessAssert.isTrue(orderPo != null
                , 40004, String.format("payment order %s not fund", context.getTradeId()));
        AppBusinessAssert.isTrue(
                PaymentStatusEnum.getByCode(orderPo.getStatusCode()) == PaymentStatusEnum.PAYMENT_SUCCESS
                , 40005, "payment status must is success,just can profit sharing");
        context.setPaymentOrder(orderPo);
    }

    @Override
    protected void checkReceiver(BaseContext<? extends BaseContext<?>> context) {
        ChannelConfigPo config = context.getChannelConfigPo();

    }

    /**
     * check payment is exists risk
     *
     * @param context context
     */
    @Override
    protected void checkRisk(BaseContext<? extends BaseContext<?>> context) {
        // TODO: 2021/9/18 risk check
    }

    private void checkIsAllowProfitSharing(BaseContext<? extends BaseContext<?>> context) {
        // TODO: 2021/9/18 check is allow profit sharing
    }
}
