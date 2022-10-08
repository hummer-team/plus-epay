package com.panli.pay.service.domain.template;

import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.service.domain.core.AbstractRefundTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.services.PaymentResultInfoService;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.YUG_BEAN_REFUND_TEMPLATE;

@Service(YUG_BEAN_REFUND_TEMPLATE)
@Slf4j
public class YugBeanRefundTemplate extends AbstractRefundTemplate {
    @Autowired
    private PaymentResultInfoService resultInfoService;

    /**
     * check it refund Does it exist risk
     *
     * @param refundContext refund context data
     */
    @Override
    protected void checkRisk(RefundContext refundContext) {
        //todo
    }

    /**
     * save to database
     *
     * @param context
     * @param refundContext
     * @param reqBodyContext
     */
    @Override
    protected void saveRefundResult(BaseResultContext<RefundResultContext> context
            , RefundContext refundContext, BasePaymentChannelReqBodyContext reqBodyContext) {
        resultInfoService.saveRefundResultOfYugBean(context.getResult(),refundContext);
    }

    @Override
    protected ChannelConfigPo checkChannelIsValid(String platformCode, String channelCode, ChannelActionEnum typeEnum) {
        //nothing
        return null;
    }
}
