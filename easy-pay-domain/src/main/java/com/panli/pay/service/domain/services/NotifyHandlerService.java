package com.panli.pay.service.domain.services;

import com.panli.pay.facade.dto.request.SendNotifyRequestDto;
import com.panli.pay.support.model.bo.NotifyDataBo;
import com.panli.pay.support.model.po.AmountFlowPo;

import java.util.Date;

public interface NotifyHandlerService {
    void addNotifyRecord(String channelCode, String body, String notifyId, String head, String eventType);

    void changePaymentStatus(String tradeId, String paymentRequestId, String channelTradeId, String channelStatus,
                             Integer status, Date paymentDate);

    void changeRefundStatus(NotifyDataBo dataBo);

    void sendNotify(SendNotifyRequestDto dto);

    void addAmountFlow(AmountFlowPo flowPo);
}
