package com.panli.pay.service.domain.template;

import com.panli.pay.facade.dto.request.WxV2NotifyRequestDto;
import com.panli.pay.service.domain.core.AbstractNotifyHandlerTemplate;
import com.panli.pay.support.model.bo.NotifyDataBo;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_V2_NOTIFY_CHANNEL;

@Service(WX_V2_NOTIFY_CHANNEL)
public class WxV2NotifyHandlerTemplate extends AbstractNotifyHandlerTemplate<WxV2NotifyRequestDto> {
    @Override
    protected NotifyDataBo parseData(WxV2NotifyRequestDto body, HttpServletRequest req) {
       throw new NotImplementedException("no impl");
    }
}
