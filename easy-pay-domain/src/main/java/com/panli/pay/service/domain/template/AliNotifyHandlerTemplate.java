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
import com.hummer.common.exceptions.AppException;
import com.panli.pay.facade.dto.request.AliNotifyRequestDto;
import com.panli.pay.service.domain.core.AbstractNotifyHandlerTemplate;
import com.panli.pay.service.domain.enums.AliTradeStatusEnum;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.support.model.bo.NotifyDataBo;
import com.panli.pay.support.model.bo.NotifyRequestBo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_NOTIFY_CHANNEL;

@Service(ALI_NOTIFY_CHANNEL)
@Slf4j
public class AliNotifyHandlerTemplate extends AbstractNotifyHandlerTemplate<Void> {
    private static final String NOTIFY_TYPE = "trade_status_sync";

    @Override
    protected NotifyDataBo parseData(Void body, HttpServletRequest req) {
        Map<String, String[]> map = req.getParameterMap();
        log.debug("ali pay notify : {}", map);
        Map<String, Object> cloneMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue().length > 0) {
                cloneMap.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        if (cloneMap.isEmpty()) {
            throw new AppException(40000, "ali pay notify body data is empty");
        }

        AliNotifyRequestDto notifyReq = JSON.parseObject(JSON.toJSONString(cloneMap), AliNotifyRequestDto.class);
        if (notifyReq == null) {
            throw new AppException(40000, "ali pay notify body convert to AliNotifyRequestDto failed");
        }

        NotifyDataBo bo = new NotifyDataBo();

        bo.setAmount(notifyReq.getTotalAmount());
        bo.setDateTime(notifyReq.getGmtPayment());
        bo.setChannelCode(PayChannelTypeEnum.ALI_PAY.name());

        if (notifyReq.getNotifyType().equalsIgnoreCase(NOTIFY_TYPE)) {
            bo.setIn(true);
            bo.setChannelStatus(notifyReq.getTradeStatus());
            bo.setChannelTradeId(notifyReq.getTradeNo());
            bo.setTradeId(notifyReq.getTradeNo());
            bo.setChannelStatusDescribe(notifyReq.getTradeStatus());
            AliTradeStatusEnum aliStatus = AliTradeStatusEnum.getByCode(notifyReq.getTradeStatus());
            PaymentStatusEnum statusEnum = PaymentStatusEnum.getByAliPayStatus(aliStatus);
            bo.setStatus(statusEnum.getCode());
            bo.setSuccess(statusEnum == PaymentStatusEnum.PAYMENT_SUCCESS);
        }

        NotifyRequestBo notifyBo = new NotifyRequestBo();
        notifyBo.setNotifyType(notifyReq.getNotifyType());
        notifyBo.setNotifyId(notifyReq.getNotifyId());
        notifyBo.setHandlerId(MDC.get(REQUEST_ID));
        notifyBo.setBody(cloneMap.toString());
        notifyBo.setChannelCode(PayChannelTypeEnum.ALI_PAY.name());

        return bo;
    }
}
