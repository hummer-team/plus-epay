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
        paymentOrderDao.updatePaymentStatus(tradeId
                , paymentRequestId
                , status
                , channelTradeId
                , channelStatus);
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
