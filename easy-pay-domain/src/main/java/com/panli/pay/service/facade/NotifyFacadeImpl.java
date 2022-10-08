package com.panli.pay.service.facade;

import com.panli.pay.facade.NotifyFacade;
import com.panli.pay.facade.dto.request.WxNotifyRequestDto;
import com.panli.pay.facade.dto.request.WxV2NotifyRequestDto;
import com.panli.pay.service.domain.lookup.LookupService;
import com.panli.pay.service.domain.services.NotifyHandlerService;
import com.panli.pay.service.domain.template.AliNotifyHandlerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_NOTIFY_CHANNEL;
import static com.panli.pay.service.domain.enums.ConstantDefine.WX_NOTIFY_CHANNEL;

@Service
@Slf4j
public class NotifyFacadeImpl implements NotifyFacade {

    @Autowired
    private LookupService lookupService;
    @Autowired
    private NotifyHandlerService handlerService;

    @Override
    public void handlerForWx(WxNotifyRequestDto dto, HttpServletRequest request) {
        lookupService.lookupNotifyHandlerTemplate(WX_NOTIFY_CHANNEL).<WxNotifyRequestDto>handler(dto, request, handlerService);
    }

    @Override
    public void handlerRefundForWxV2(WxV2NotifyRequestDto dto, HttpServletRequest request) {
        lookupService.lookupNotifyHandlerTemplate(WX_NOTIFY_CHANNEL).<WxV2NotifyRequestDto>handler(dto, request, handlerService);
    }

    /**
     * handler ali payment callback
     *
     * @param req http servlet
     */
    @Override
    public void handlerNotifyForAli(HttpServletRequest req) {
        lookupService.lookupNotifyHandlerTemplate(ALI_NOTIFY_CHANNEL).<AliNotifyHandlerTemplate>handler(Void.class
                , req, handlerService);
    }
}
