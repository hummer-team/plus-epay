package com.panli.pay.facade;

import com.panli.pay.facade.dto.request.WxNotifyRequestDto;
import com.panli.pay.facade.dto.request.WxV2NotifyRequestDto;

import javax.servlet.http.HttpServletRequest;

public interface NotifyFacade {
    /**
     * handler weixin callback
     *
     * @param dto     weix request message
     * @param request http servlet
     */
    void handlerForWx(WxNotifyRequestDto dto, HttpServletRequest request);

    /**
     * handler weixin callback from api v2 version
     *
     * @param dto     weix request message
     * @param request http servlet
     */
    void handlerRefundForWxV2(WxV2NotifyRequestDto dto, HttpServletRequest request);

    /**
     * handler ali payment callback
     *
     * @param req http servlet
     */
    void handlerNotifyForAli(HttpServletRequest req);
}
