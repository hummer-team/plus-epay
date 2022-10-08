package com.panli.pay.service.domain.core;

import com.panli.pay.dao.AmountFlowDao;
import com.panli.pay.facade.dto.request.SendNotifyRequestDto;
import com.panli.pay.service.domain.services.NotifyHandlerService;
import com.panli.pay.support.model.bo.NotifyDataBo;
import com.panli.pay.support.model.po.AmountFlowPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * @author edz
 */
@Slf4j
public abstract class AbstractNotifyHandlerTemplate<T> {
    @Autowired
    private AmountFlowDao flowDao;

    public void handler(T body, HttpServletRequest req, NotifyHandlerService handlerService) {
        //step1,parse
        NotifyDataBo dataBo = parseData(body, req);

        Assert.notNull(dataBo.getNotifyRequestBo(), "notify request data can't null");
        //step2,add callback  log
        handlerService.addNotifyRecord(dataBo.getNotifyRequestBo().getChannelCode()
                , dataBo.getNotifyRequestBo().getBody(), dataBo.getNotifyRequestBo().getNotifyId()
                , dataBo.getNotifyRequestBo().getHead(), dataBo.getNotifyRequestBo().getNotifyType());
        //step4,
        if (existsOrderFlow(dataBo)) {
            log.info("this pay order {} already handler,channel code {}  notify type {}"
                    , dataBo.getTradeId(), dataBo.getChannelCode(), dataBo.getNotifyRequestBo().getNotifyType());
            return;
        }
        //step4,update status
        changeStatus(handlerService, dataBo);
        //step5，add amount flow
        addFlowIfSuccess(handlerService, dataBo);
        //step6,send notify
        SendNotifyRequestDto notifyDto = new SendNotifyRequestDto();
        notifyDto.setDateTime(dataBo.getDateTime());
        notifyDto.setStatus(dataBo.getStatus());
        notifyDto.setNotifyType(dataBo.isIn() ? "in" : "out");
        notifyDto.setTradeId(dataBo.getTradeId());
        notifyDto.setStatusDescribe(dataBo.getStatusDescribe());
        handlerService.sendNotify(notifyDto);
    }

    private boolean existsOrderFlow(NotifyDataBo dataBo) {
        AmountFlowPo flowPo = flowDao.queryOrderFlow(dataBo.getTradeId(), dataBo.getChannelTradeId());
        return flowPo != null;
    }

    private void changeStatus(NotifyHandlerService handlerService, NotifyDataBo dataBo) {
        if (dataBo.isIn()) {
            handlerService.changePaymentStatus(dataBo.getTradeId()
                    , dataBo.getPaymentRequestId(), dataBo.getChannelTradeId()
                    , dataBo.getChannelStatus(), dataBo.getStatus(), dataBo.getDateTime());
            return;
        }
        handlerService.changeRefundStatus(dataBo);
    }

    private void addFlowIfSuccess(NotifyHandlerService handlerService, NotifyDataBo dataBo) {
        if (dataBo.isSuccess()) {
            AmountFlowPo flowPo = new AmountFlowPo();
            flowPo.setTradeId(dataBo.getTradeId());
            flowPo.setChannelTradeId(dataBo.getChannelTradeId());
            flowPo.setAmount(dataBo.getAmount());
            flowPo.setFlowType(!dataBo.isIn());
            handlerService.addAmountFlow(flowPo);
        }
    }

    protected abstract NotifyDataBo parseData(T body, HttpServletRequest req);
}
