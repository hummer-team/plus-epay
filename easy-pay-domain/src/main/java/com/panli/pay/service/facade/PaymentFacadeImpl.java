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

package com.panli.pay.service.facade;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.AppBusinessAssert;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.dao.ChannelPlatformConfigDao;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.BasePaymentCancelRequestDto;
import com.panli.pay.facade.dto.request.BasePaymentRequestDto;
import com.panli.pay.facade.dto.request.BaseProfitSharingOrderRequestDto;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.facade.dto.request.RefundRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.facade.dto.response.QueryPaymentStatusCommonRespDto;
import com.panli.pay.service.domain.context.CancelContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentQueryResultContext;
import com.panli.pay.service.domain.context.ProfitSharingContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.core.AbstractCanelPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentQueryTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractProfitSharingTemplate;
import com.panli.pay.service.domain.core.AbstractRefundTemplate;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.OrderTypeEnum;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.enums.TemplateEnum;
import com.panli.pay.service.domain.lookup.LookupService;
import com.panli.pay.service.domain.services.NameBuilderService;
import com.panli.pay.support.model.po.ChannelPlatformConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * this payment service
 */
@Service
public class PaymentFacadeImpl implements PaymentFacade {
    @Autowired
    private LookupService lookupService;
    @Autowired
    private PaymentOrderDao paymentOrderDao;
    @Autowired
    private ChannelPlatformConfigDao platformConfigDao;

    @Override

    public BasePaymentResp<? extends BasePaymentResp<?>> payment(BasePaymentRequestDto dto) {
        @SuppressWarnings({"rawtypes"})
        PaymentChannel payment = lookupService.lookupChannel(dto.getPays().getChannelCode(), ChannelActionEnum.PAY);

        AbstractPaymentTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                , dto.getPays().getChannelCode(), TemplateEnum.PAY);

        //builder context
        PaymentContext context = ObjectCopyUtils.copy(dto, PaymentContext.class);
        context.setChannelCode(dto.getPays().getChannelCode());
        context.setAmount(dto.getPays().getAmount());
        context.setAffixData(dto.getPays().getParameter());
        context.setPayChannelType(PayChannelTypeEnum.getByChannelCode(dto.getPays().getChannelCode()));
        context.setMerchantId(dto.getMerchantId());
        context.setOrderType(getOrderType(dto));
        context.setContext(context);

        //do pay
        return template.doPayment(context, payment);
    }

    @Override
    public BasePaymentResp<? extends BasePaymentResp<?>> createProfitSharing(
            BaseProfitSharingOrderRequestDto<? extends BaseProfitSharingOrderRequestDto<?>> dto) {

        String orderPayChannel = NameBuilderService.getPaymentOrderChannel(dto.getChannelCode(), ChannelActionEnum.PAY);
        PaymentOrderPo orderPo = paymentOrderDao.queryOneByTradeIdAndCode(dto.getTradeId(), orderPayChannel);
        AppBusinessAssert.isTrue(orderPo != null
                , 40004, String.format("payment order %s not fund", dto.getTradeId()));
        AppBusinessAssert.isTrue(
                PaymentStatusEnum.getByCode(orderPo.getStatusCode()) == PaymentStatusEnum.PAYMENT_SUCCESS
                , 40005, "payment status must is success,just can profit sharing");

        AbstractProfitSharingTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                , dto.getChannelCode(), TemplateEnum.PROFIT_SHARING_REQUEST);

        @SuppressWarnings({"rawtypes"})
        PaymentChannel paymentChannel = lookupService.lookupChannel(dto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_REQUEST);

        ProfitSharingContext context = new ProfitSharingContext();

        context.setTradeId(dto.getTradeId());
        context.getAffixData().put("dto", dto);
        context.setChannelCode(dto.getChannelCode());
        context.setPlatformCode(orderPo.getPlatformCode());
        context.setUserId(dto.getUserId());
        context.setContext(context);
        context.setPaymentOrder(orderPo);

        return template.doPayment(context, paymentChannel);
    }

    @Override
    public List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>>
    queryPayment(BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto) {

        List<PaymentOrderPo> orderPos = Strings.isNullOrEmpty(dto.getPlatformCode())
                ? paymentOrderDao.queryByTradeId(dto.getTradeId())
                : paymentOrderDao.queryByTradeIdAndCode(dto.getTradeId(), dto.getPlatformCode());
        if (CollectionUtils.isEmpty(orderPos)) {
            throw new AppException(40004
                    , "trade id invalid: " + dto.getTradeId() + " channelCode: " + dto.getPlatformCode());
        }

        List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> respDtos = Lists.newArrayListWithCapacity(orderPos.size());
        for (PaymentOrderPo order : orderPos) {
            dto.setChannelCode(order.getChannelCode());
            QueryPaymentStatusCommonRespDto respDto = new QueryPaymentStatusCommonRespDto();
            PaymentStatusEnum statusEnum = PaymentStatusEnum.getByCode(order.getStatusCode());
            respDto.setTradeId(order.getTradeId());
            respDto.setStatus(order.getStatusCode());
            respDto.setDescribe(PaymentStatusEnum.getByCode(order.getStatusCode()).getCode2());
            //if payment success then return
            if (PaymentStatusEnum.PAYMENT_SUCCESS == statusEnum) {
                respDtos.add(respDto);
                continue;
            }

            //template & channel
            AbstractPaymentQueryTemplate queryTemplate = lookupService.lookupTemplate(order.getPlatformCode()
                    , order.getChannelCode(), TemplateEnum.QUERY);

            @SuppressWarnings({"rawtypes"})
            PaymentChannel paymentChannel = lookupService.lookupChannel(
                    order.getChannelCode()
                    , ChannelActionEnum.PAY_QUERY);
            //do  query channel pay
            PaymentQueryResultContext resultContext = queryTemplate.doQuery(dto, paymentChannel);

            respDto.setDescribe(String.format("%s|%s", respDto.getDescribe(), resultContext.getDescribe()));

            respDto.setChannelStatus(resultContext.getStatus());
            respDto.setChanneldescribe(String.format("%s|%s|%s", resultContext.getChannelSubMessage()
                    , resultContext.getChannelSubCode(), resultContext.getChannelRespMessage()));
            respDto.setChannelTradeId(resultContext.getChannelTradeId());
            respDto.setChannelCode(order.getChannelCode());

            respDtos.add(respDto);
        }
        return respDtos;
    }

    @Override
    public List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> queryPaymentByTradeId(String tradeId
            , String platformCode) {
        BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto
                = new BaseQueryPaymentStatusRequestDto<>();
        dto.setTradeId(tradeId);
        dto.setPlatformCode(platformCode);
        return queryPayment(dto);
    }

    @Override

    public void refund(RefundRequestDto dto) {
        List<PaymentOrderPo> orderPos = paymentOrderDao.queryByTradeId(dto.getTradeId());
        //todo
        RefundContext context = ObjectCopyUtils.copy(dto, RefundContext.class);
        context.setContext(context);

        AbstractRefundTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                , dto.getRefundToChannelCode(), TemplateEnum.REFUND);
        @SuppressWarnings({"rawtypes"})
        PaymentChannel paymentChannel = lookupService.lookupChannel(dto.getRefundToChannelCode(), ChannelActionEnum.REFUND);
        template.doRefund(context, paymentChannel);
    }

    @Override
    public void cancel(BasePaymentCancelRequestDto dto) {
        List<PaymentOrderPo> orderPos = paymentOrderDao.queryByTradeId(dto.getTradeId());
        if (CollectionUtils.isEmpty(orderPos)) {
            throw new AppException(40004, String.format("trade %s invalid", dto.getTradeId()));
        }

        CancelContext context = ObjectCopyUtils.copy(dto, CancelContext.class);
        context.setContext(context);

        ChannelPlatformConfigPo configPo = platformConfigDao.queryByCode(dto.getPlatformCode(), dto.getChannelCode());

        for (PaymentOrderPo order : orderPos) {
            AbstractCanelPaymentTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                    , order.getChannelCode(), TemplateEnum.CANCEL);
            // TODO: 2021/7/30 query channel code

            @SuppressWarnings({"rawtypes"})
            PaymentChannel paymentChannel = lookupService.lookupChannel(order.getChannelCode(), ChannelActionEnum.CANCEL);
            template.doCancel(context, paymentChannel);
        }
    }

    private OrderTypeEnum getOrderType(BasePaymentRequestDto dto) {
        return dto.getPays().getParameter() != null
                && dto.getPays().getParameter().containsKey("subMchId")
                ? OrderTypeEnum.PROFIT_SHARING_PAYMENT_ORDER
                : OrderTypeEnum.PAYMENT_ORDER;
    }
}