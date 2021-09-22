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
