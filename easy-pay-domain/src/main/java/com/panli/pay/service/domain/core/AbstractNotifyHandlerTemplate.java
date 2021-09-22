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

package com.panli.pay.service.domain.core;

import com.panli.pay.dao.AmountFlowDao;
import com.panli.pay.facade.dto.request.SendNotifyRequestDto;
import com.panli.pay.service.domain.enums.FlowTypeEnum;
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
            flowPo.setFlowType(dataBo.isIn() ? FlowTypeEnum.IN.getCode() : FlowTypeEnum.OUT.getCode());
            handlerService.addAmountFlow(flowPo);
        }
    }

    protected abstract NotifyDataBo parseData(T body, HttpServletRequest req);
}
