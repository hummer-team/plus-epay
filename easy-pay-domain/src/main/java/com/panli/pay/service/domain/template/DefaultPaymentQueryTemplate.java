package com.panli.pay.service.domain.template;

import com.hummer.common.exceptions.AppException;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.core.AbstractPaymentQueryTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_QUERY_TEMPLATE;

@Service(DEFAULT_QUERY_TEMPLATE)
public class DefaultPaymentQueryTemplate extends AbstractPaymentQueryTemplate {
    @Autowired
    private PaymentOrderDao paymentOrderDao;

    @Override
    protected PaymentQueryContext setContextAndSelfCheck(
            BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto) {
        PaymentOrderPo orderPo = paymentOrderDao.queryOneByTradeIdAndCode(dto.getPlatformCode(), dto.getTradeId());
        if (orderPo == null) {
            throw new AppException(40004, "trade id invalid " + dto.getTradeId());
        }

        ChannelConfigPo channelConfigPo = checkChannelIsValid(orderPo.getPlatformCode(), dto.getChannelCode()
                , ChannelActionEnum.PAY_QUERY);

        PaymentQueryContext context = PaymentQueryContext.builder()
                .platformCode(orderPo.getPlatformCode())
                .channelCode(orderPo.getChannelCode())
                .platformSubType(orderPo.getPlatformSubType())
                .tradeId(dto.getTradeId())
                .channelConfigPo(channelConfigPo)
                .refundBatchId(orderPo.getRefundBatchId())
                .channelTradeId(orderPo.getChannelTradeId())
                .build();

        context.setContext(context);
        return context;
    }
}
