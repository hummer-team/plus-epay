package com.panli.pay.service.facade;

import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.BasePaymentCancelRequestDto;
import com.panli.pay.facade.dto.request.BasePaymentRequestDto;
import com.panli.pay.facade.dto.request.BaseProfitSharingOrderRequestDto;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.facade.dto.request.ProfitSharingRateReqDto;
import com.panli.pay.facade.dto.request.ProfitSharingReturnReqDto;
import com.panli.pay.facade.dto.request.ProfitSharingUnfreezeReqDto;
import com.panli.pay.facade.dto.request.RefundRequestDto;
import com.panli.pay.facade.dto.request.ServiceMerchantAddReceiverReqDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.facade.dto.response.ProfitSharingRateQueryRespDto;
import com.panli.pay.facade.dto.response.QueryPaymentStatusCommonRespDto;
import com.panli.pay.service.domain.context.PaymentCancelContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.ProfitSharingAddReceiverContext;
import com.panli.pay.service.domain.context.ProfitSharingContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.core.AbstractCancelPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractChannelTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentQueryTemplate;
import com.panli.pay.service.domain.core.AbstractPaymentTemplate;
import com.panli.pay.service.domain.core.AbstractProfitSharingAddReceiverTemplate;
import com.panli.pay.service.domain.core.AbstractProfitSharingTemplate;
import com.panli.pay.service.domain.core.AbstractRefundTemplate;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.enums.OrderTypeEnum;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.enums.TemplateEnum;
import com.panli.pay.service.domain.lookup.LookupService;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.PaymentCancelResultContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.result.ProfitSharingAddReceiverResultContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.support.model.bo.payment.BasePaymentCancelResp;
import com.panli.pay.support.model.bo.payment.WxServiceMerchantAddReceiverRespDto;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        context.setOrderType(OrderTypeEnum.PAYMENT_ORDER);
        context.setContext(context);

        //do pay
        return template.doPayment(context, payment);
    }

    @Override
    public BasePaymentResp<? extends BasePaymentResp<?>> createProfitSharing(
            BaseProfitSharingOrderRequestDto<? extends BaseProfitSharingOrderRequestDto<?>> dto) {

        AbstractProfitSharingTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                , dto.getChannelCode(), TemplateEnum.PROFIT_SHARING_REQUEST);

        @SuppressWarnings({"rawtypes"})
        PaymentChannel paymentChannel = lookupService.lookupChannel(dto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_REQUEST);

        ProfitSharingContext context = new ProfitSharingContext();

        context.setTradeId(dto.getTradeId());
        context.getAffixData().put("dto", dto);
        context.setChannelCode(dto.getChannelCode());
        context.setPlatformCode(dto.getPlatformCode());
        context.setUserId(dto.getUserId());
        context.setContext(context);
        return template.doPayment(context, paymentChannel);
    }

    @Override
    public List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>>
    queryPayment(BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto) {

        List<PaymentOrderPo> orderPos = StringUtils.isEmpty(dto.getPlatformCode())
                ? paymentOrderDao.queryByTradeId(dto.getTradeId())
                : paymentOrderDao.queryByTradeIdAndCode(dto.getTradeId(), dto.getPlatformCode());
        if (CollectionUtils.isEmpty(orderPos)) {
            throw new AppException(40004
                    , "trade id invalid: " + dto.getTradeId() + " channelCode: " + dto.getPlatformCode());
        }

        List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> respDtos = new ArrayList<>(orderPos.size());
        for (PaymentOrderPo order : orderPos) {
            dto.setChannelCode(order.getChannelCode());
            QueryPaymentStatusCommonRespDto respDto = new QueryPaymentStatusCommonRespDto();
            PaymentStatusEnum statusEnum = PaymentStatusEnum.getByCode(order.getStatusCode());
            respDto.setTradeId(order.getTradeId());
            respDto.setStatus(order.getStatusCode());
            respDto.setDescribe(PaymentStatusEnum.getByCode(order.getStatusCode()).getCode2());
            respDto.setChannelTradeId(order.getChannelTradeId());
            respDto.setAmount(order.getAmount());
            respDto.setMerchantId(order.getMerchantId());
            respDto.setChannelCode(order.getChannelCode());
            //if payment success then return
            if (PaymentStatusEnum.WAIT_PAYMENT != statusEnum && PaymentStatusEnum.UNKNOWN != statusEnum) {
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

            respDto.setChannelStatus(resultContext.getChannelSubCode());
            respDto.setChanneldescribe(String.format("%s|%s|%s", resultContext.getChannelSubMessage()
                    , resultContext.getChannelSubCode(), resultContext.getChannelRespMessage()));
            respDto.setChannelTradeId(resultContext.getChannelTradeId());
            respDto.setChannelCode(order.getChannelCode());
            respDto.setAmount(order.getAmount());
            respDto.setAmountUnitType(0);
            respDto.setMerchantId(order.getMerchantId());
            respDto.setStatus(resultContext.getStatus().getCode());
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
    public String refund(RefundRequestDto dto) {
        RefundContext context = ObjectCopyUtils.copy(dto, RefundContext.class);
        context.setContext(context);

        AbstractRefundTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                , dto.getChannelCode(), TemplateEnum.REFUND);
        @SuppressWarnings({"rawtypes"})
        PaymentChannel paymentChannel = lookupService.lookupChannel(dto.getChannelCode(), ChannelActionEnum.REFUND);
        BaseResultContext<RefundResultContext> resultContext = template.doRefund(context, paymentChannel);
        return resultContext == null ? null : resultContext.getResult().getChannelRefundId();
    }

    @Override
    public List<BasePaymentCancelResp> cancel(BasePaymentCancelRequestDto dto) {
        List<PaymentOrderPo> orderPos = paymentOrderDao.queryByTradeId(dto.getTradeId());
        if (CollectionUtils.isEmpty(orderPos)) {
            throw new AppException(40004, String.format("trade %s invalid", dto.getTradeId()));
        }
        PaymentCancelContext context = ObjectCopyUtils.copy(dto, PaymentCancelContext.class);
        context.setContext(context);
        List<BasePaymentCancelResp> results = new ArrayList<>();
        for (PaymentOrderPo order : orderPos) {
            AbstractCancelPaymentTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                    , order.getChannelCode(), TemplateEnum.CANCEL);

            @SuppressWarnings({"rawtypes"})
            PaymentChannel paymentChannel = lookupService.lookupChannel(order.getChannelCode(), ChannelActionEnum.CANCEL);
            PaymentCancelResultContext resultContext = template.doCancel(context, paymentChannel);
            BasePaymentCancelResp resp = new BasePaymentCancelResp();
            resp.setSuccess(resultContext.isSuccess());
            resp.setRecall(resultContext.isRecall());
            resp.setChannelRespCode(resultContext.getChannelRespCode());
            resp.setChannelRespDesc(resultContext.getChannelRespMessage());
            results.add(resp);
        }
        return results;
    }

    @Override
    public WxServiceMerchantAddReceiverRespDto addReceivers(ServiceMerchantAddReceiverReqDto dto) {

        AbstractProfitSharingAddReceiverTemplate template = lookupService.lookupTemplate(dto.getPlatformCode()
                , dto.getChannelCode(), TemplateEnum.ADD_RECEIVER);

        @SuppressWarnings({"rawtypes"})
        PaymentChannel paymentChannel = lookupService.lookupChannel(dto.getChannelCode()
                , ChannelActionEnum.ADD_RECEIVER);
        ProfitSharingAddReceiverContext context = ObjectCopyUtils.copy(dto, ProfitSharingAddReceiverContext.class);
        context.setContext(context);

        BaseResultContext<ProfitSharingAddReceiverResultContext> resultContext = template.doAddReceiver(context, paymentChannel);
        WxServiceMerchantAddReceiverRespDto resp = new WxServiceMerchantAddReceiverRespDto();
        resp.setSuccess(resultContext.isSuccess());
        return resp;
    }

    @Override
    public ProfitSharingRateQueryRespDto queryProfitSharingRate(ProfitSharingRateReqDto reqDto) {
        AbstractChannelTemplate template = lookupService.lookupTemplate(reqDto.getPlatformCode()
                , reqDto.getChannelCode(), TemplateEnum.PROFIT_SHARING_RATE_QUERY);
        PaymentChannel paymentChannel = lookupService.lookupChannel(reqDto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_RATE_QUERY);
        BaseResultContext context = template.doAction(reqDto, paymentChannel);
        return ObjectCopyUtils.copy(context, ProfitSharingRateQueryRespDto.class);
    }

    @Override
    public void unfreezeProfitSharingOrder(ProfitSharingUnfreezeReqDto reqDto) {

        AbstractChannelTemplate template = lookupService.lookupTemplate(reqDto.getPlatformCode()
                , reqDto.getChannelCode(), TemplateEnum.PROFIT_SHARING_UNFREEZE);
        PaymentChannel paymentChannel = lookupService.lookupChannel(reqDto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_UNFREEZE);
        template.doAction(reqDto, paymentChannel);
    }

    @Override
    public String returnProfitSharing(ProfitSharingReturnReqDto reqDto) {

        AbstractChannelTemplate template = lookupService.lookupTemplate(reqDto.getPlatformCode()
                , reqDto.getChannelCode(), TemplateEnum.PROFIT_SHARING_RETURN);
        PaymentChannel paymentChannel = lookupService.lookupChannel(reqDto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_RETURN);
        RefundResultContext context = (RefundResultContext) template.doAction(reqDto, paymentChannel);
        return context.getChannelRefundId();
    }
}