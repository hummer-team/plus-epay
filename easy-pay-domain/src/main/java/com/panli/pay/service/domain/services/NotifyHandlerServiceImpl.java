package com.panli.pay.service.domain.services;

import com.google.common.base.Strings;
import com.panli.pay.dao.AmountFlowDao;
import com.panli.pay.dao.NotifyRecordDao;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.facade.dto.request.SendNotifyRequestDto;
import com.panli.pay.support.model.bo.NotifyDataBo;
import com.panli.pay.support.model.po.AmountFlowPo;
import com.panli.pay.support.model.po.NotifyRecordPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.hummer.common.SysConstant.REQUEST_ID;

@Service
public class NotifyHandlerServiceImpl implements NotifyHandlerService {
    @Autowired
    private NotifyRecordDao notifyRecordDao;
    @Autowired
    private PaymentOrderDao paymentOrderDao;
    @Autowired
    private AmountFlowDao amountFlowDao;

    @Override
    public void addNotifyRecord(String channelCode, String body, String notifyId, String head, String eventType) {
        NotifyRecordPo recordPo = new NotifyRecordPo();
        recordPo.setBody(body);
        recordPo.setChannelCode(channelCode);
        recordPo.setNotifyId(notifyId);
        recordPo.setHandlerId(MDC.get(REQUEST_ID));
        recordPo.setHead(head);
        recordPo.setNotifyType(eventType);
        notifyRecordDao.insert(recordPo);
    }

    @Override
    public void changePaymentStatus(String tradeId, String paymentRequestId, String channelTradeId, String channelStatus
            , Integer status, Date paymentDate) {

        PaymentOrderPo po = new PaymentOrderPo();
        po.setTradeId(tradeId);
        po.setRequestId(paymentRequestId);
        po.setChannelTradeId(channelTradeId);
        po.setChannelTradeStatus(channelStatus);
        po.setStatusCode(status);
        po.setPaymentDateTime(paymentDate);
        paymentOrderDao.updatePaymentStatus(po);
    }

    @Override
    public void changeRefundStatus(NotifyDataBo dataBo) {
        paymentOrderDao.updateRefundStatus(dataBo.getTradeId()
                , dataBo.getStatus()
                , dataBo.getChannelTradeId(), dataBo.getChannelRefundId()
                , dataBo.getDateTime()
                , dataBo.getChannelStatus());
    }

    @Override
    public void sendNotify(SendNotifyRequestDto dto) {
        //todo
    }

    @Override
    public void addAmountFlow(AmountFlowPo flowPo) {
        if (Strings.isNullOrEmpty(flowPo.getMerchantId())) {
            PaymentOrderPo payOrderPo = paymentOrderDao.queryOutChannelOrder(flowPo.getChannelTradeId());
            flowPo.setMerchantId(payOrderPo.getMerchantId());
        }
        amountFlowDao.insert(flowPo);
    }
}
