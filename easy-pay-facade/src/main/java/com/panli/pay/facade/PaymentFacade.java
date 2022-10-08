package com.panli.pay.facade;

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
import com.panli.pay.support.model.bo.payment.BasePaymentCancelResp;
import com.panli.pay.support.model.bo.payment.WxServiceMerchantAddReceiverRespDto;

import java.util.List;

public interface PaymentFacade {
    BasePaymentResp<? extends BasePaymentResp<?>> payment(BasePaymentRequestDto dto);

    BasePaymentResp<? extends BasePaymentResp<?>> createProfitSharing(
            BaseProfitSharingOrderRequestDto<? extends BaseProfitSharingOrderRequestDto<?>> dto);

    List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> queryPaymentByTradeId(String tradeId, String platformCode);

    List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>>
    queryPayment(BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto);

    String refund(RefundRequestDto dto);

    List<BasePaymentCancelResp> cancel(BasePaymentCancelRequestDto dto);

    WxServiceMerchantAddReceiverRespDto addReceivers(ServiceMerchantAddReceiverReqDto dto);

    ProfitSharingRateQueryRespDto queryProfitSharingRate(ProfitSharingRateReqDto reqDto);

    void unfreezeProfitSharingOrder(ProfitSharingUnfreezeReqDto reqDto);

    String returnProfitSharing(ProfitSharingReturnReqDto reqDto);
}
