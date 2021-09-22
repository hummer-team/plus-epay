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

package com.panli.pay.service.domain.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.security.Aes;
import com.panli.pay.facade.dto.request.WxNotifyPaymentRealData;
import com.panli.pay.facade.dto.request.WxNotifyRefundRealData;
import com.panli.pay.facade.dto.request.WxNotifyRequestDto;
import com.panli.pay.service.domain.core.AbstractNotifyHandlerTemplate;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.enums.WxRefundStatusEnum;
import com.panli.pay.service.domain.enums.WxTradeStatusEnum;
import com.panli.pay.service.domain.services.WxResponseService;
import com.panli.pay.support.model.bo.NotifyDataBo;
import com.panli.pay.support.model.bo.NotifyRequestBo;
import com.panli.pay.support.utils.HttpServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_NOTIFY_CHANNEL;

@Service(WX_NOTIFY_CHANNEL)
@Slf4j
public class WxNotifyHandlerTemplate extends AbstractNotifyHandlerTemplate<WxNotifyRequestDto> {
    private static final String TRANSACTION = "TRANSACTION";
    private static final String REFUND = "REFUND";
    @Autowired
    private WxResponseService wxResponseService;

    @Override
    protected NotifyDataBo parseData(WxNotifyRequestDto body, HttpServletRequest req) {
        NotifyDataBo dataBo = new NotifyDataBo();
        String originBody;
        if (TRANSACTION.startsWith(body.getEventType())) {
            WxNotifyPaymentRealData realData = parseWxNotifyRealDataAndSign(body, req
                    , new TypeReference<WxNotifyPaymentRealData>() {
                    });
            originBody = JSON.toJSONString(realData);
            dataBo.setAmount(BigDecimal.valueOf(realData.getAmount().getTotal())
                    .divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP));
            dataBo.setChannelTradeId(realData.getTransactionId());
            dataBo.setIn(true);
            dataBo.setChannelStatus(realData.getTradeState());
            dataBo.setChannelStatusDescribe(realData.getTradeStateDesc());
            dataBo.setDateTime(realData.getSuccessTime());
            dataBo.setTradeId(realData.getOutTradeNo());
            String paymentRequestId = Aes.decryptByDefaultKeyIv(realData.getAttach());
            dataBo.setPaymentRequestId(paymentRequestId);
            WxTradeStatusEnum wxStatus = WxTradeStatusEnum.getByCode(realData.getTradeState());
            PaymentStatusEnum tradeStatus = PaymentStatusEnum.getByWxPaymentStatus(wxStatus);
            dataBo.setSuccess(tradeStatus == PaymentStatusEnum.PAYMENT_SUCCESS);
            dataBo.setStatus(tradeStatus.getCode());

        } else if (REFUND.startsWith(body.getEventType())) {
            WxNotifyRefundRealData realData = parseWxNotifyRealDataAndSign(body, req
                    , new TypeReference<WxNotifyRefundRealData>() {
                    });
            originBody = JSON.toJSONString(realData);
            dataBo.setIn(false);
            dataBo.setTradeId(realData.getOutTradeNo());
            dataBo.setChannelTradeId(realData.getTransactionId());
            dataBo.setChannelRefundId(realData.getRefundId());
            dataBo.setRefundBatchId(realData.getOutRefundNo());
            dataBo.setAmount(BigDecimal.valueOf(realData.getAmount().getRefund())
                    .divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP));
            dataBo.setDateTime(realData.getSuccessTime());
            dataBo.setChannelStatus(realData.getRefundStatus());
            WxRefundStatusEnum refundStatus = WxRefundStatusEnum.getByCode(realData.getRefundStatus());
            PaymentStatusEnum status = PaymentStatusEnum.getByWxRefundStatus(refundStatus);
            dataBo.setSuccess(status == PaymentStatusEnum.PAYMENT_SUCCESS);
            dataBo.setStatus(status.getCode());
        } else {
            throw new AppException(40004, "not support event type " + body.getEventType());
        }

        NotifyRequestBo notifyRequestBo = new NotifyRequestBo();
        notifyRequestBo.setNotifyId(body.getId());
        notifyRequestBo.setNotifyType(body.getEventType());
        notifyRequestBo.setHandlerId(MDC.get(REQUEST_ID));
        notifyRequestBo.setChannelCode("wx");
        notifyRequestBo.setBody(JSON.toJSONString(body));
        notifyRequestBo.setDecryptBody(originBody);
        notifyRequestBo.setHead(HttpServletUtil.getAllHeadOfString(req));
        dataBo.setNotifyRequestBo(notifyRequestBo);

        return dataBo;
    }

    private <T> T parseWxNotifyRealDataAndSign(WxNotifyRequestDto dto, HttpServletRequest request
            , TypeReference<T> tClass) {
        String jsonBody = JSON.toJSONString(dto);
        //add notify log
        //notifyRecordService.addNotifyRecord("wx", jsonBody, dto.getId()
        //        , HttpServletUtil.getAllHeadOfString(request).toString(), dto.getEventType());
        if (!wxResponseService.verify(request, jsonBody)) {
            log.warn("wx notify sign verify failed, req info is : {}", dto);
            throw new AppException(40005, "invalid notify");
        }

        String realBody = wxResponseService.tryDecryptBody(dto.getResource().getAssociatedData()
                , dto.getResource().getNonce(), dto.getResource().getCiphertext());
        log.info("wx notify real body is {}", realBody);
        if (Strings.isNullOrEmpty(realBody)) {
            log.warn("wx notify decrypt failed,notify body is : {}", jsonBody);
            throw new AppException(40006, "decrypt body failed");
        }

        return JSON.parseObject(realBody, tClass);
    }
}
