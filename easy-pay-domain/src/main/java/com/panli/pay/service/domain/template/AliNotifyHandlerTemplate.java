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
