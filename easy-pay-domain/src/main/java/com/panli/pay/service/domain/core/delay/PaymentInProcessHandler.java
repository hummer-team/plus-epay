package com.panli.pay.service.domain.core.delay;

import com.hummer.common.SysConstant;
import com.hummer.common.utils.DateUtil;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.BasePaymentCancelRequestDto;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.facade.dto.request.ServiceMerchantBarCodePayQueryRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.event.bo.PaymentInProcessBo;
import com.panli.pay.service.domain.services.NotifyHandlerService;
import com.panli.pay.support.model.bo.payment.BasePaymentCancelResp;
import com.panli.pay.support.model.po.AmountFlowPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * PaymentInProcessHandler
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/9/24 15:50
 */
@Component
public class PaymentInProcessHandler {
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private NotifyHandlerService notifyHandlerService;

    public boolean handle(PaymentInProcessBo t) {
        BaseQueryPaymentStatusRequestDto<ServiceMerchantBarCodePayQueryRequestDto> reqDto
                = new BaseQueryPaymentStatusRequestDto<>();
        reqDto.setChannelCode(t.getChannelCode());
        reqDto.setPlatformCode(t.getPlatformCode());
        reqDto.setTradeId(t.getTradeId());
        ServiceMerchantBarCodePayQueryRequestDto barCode = new ServiceMerchantBarCodePayQueryRequestDto();
        barCode.setSubAppId(t.getSubAppId());
        barCode.setSubMchId(t.getSubMchId());
        reqDto.setData(barCode);

        // query order
        List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> resp = paymentFacade.queryPayment(reqDto);
        BasePaymentQueryResp<? extends BasePaymentQueryResp<?>> order = resp.get(0);
        // pay success
        Date now = DateUtil.now();
        String requestId = MDC.get(SysConstant.REQUEST_ID);
        if (PaymentStatusEnum.PAYMENT_SUCCESS.getCode() == order.getStatus()) {
            notifyHandlerService.changePaymentStatus(order.getTradeId(), requestId
                    , order.getChannelTradeId(), order.getChannelStatus(), order.getStatus(), DateUtil.now());
            AmountFlowPo po = new AmountFlowPo();
            po.setAmount(order.getAmount());
            po.setMerchantId(order.getMerchantId());
            po.setAmountUnitType(order.getAmountUnitType());
            po.setBatchId(null);
            po.setChannelTradeId(order.getChannelTradeId());
            po.setCreatedDatetime(now);
            po.setFlowType(false);
            po.setRate(1d);
            po.setRequestId(requestId);
            po.setTradeId(order.getTradeId());
            notifyHandlerService.addAmountFlow(po);
            return true;
        } else if (PaymentStatusEnum.PAYMENT_CANCELED.getCode() == order.getStatus()
                || PaymentStatusEnum.PAYMENT_CLOSED.getCode() == order.getStatus()
                || PaymentStatusEnum.PAYMENT_TIMEOUT.getCode() == order.getStatus()
                || PaymentStatusEnum.REFUND_FAILED.getCode() == order.getStatus()
                || PaymentStatusEnum.ORDER_NOT_EXIST.getCode() == order.getStatus()
        ) {
            // update fail
            notifyHandlerService.changePaymentStatus(order.getTradeId(), requestId
                    , order.getChannelTradeId(), order.getChannelStatus(), order.getStatus(), now);
            return true;
        } else if (now.after(t.getPaymentOutTime())) {
            // expire cancel
            BasePaymentCancelRequestDto dto = new BasePaymentCancelRequestDto();
            dto.setChannelCode(t.getChannelCode());
            dto.setPlatformCode(t.getPlatformCode());
            dto.setSubAppId(t.getSubAppId());
            dto.setSubMchId(t.getSubMchId());
            dto.setTradeId(t.getTradeId());
            dto.setUserId(t.getUserId());
            List<BasePaymentCancelResp> results = paymentFacade.cancel(dto);
            BasePaymentCancelResp result = results.get(0);
            return !result.isRecall();
        }
        return false;
    }
}
