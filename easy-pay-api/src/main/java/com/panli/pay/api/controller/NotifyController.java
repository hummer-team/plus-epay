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

package com.panli.pay.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.panli.pay.facade.NotifyFacade;
import com.panli.pay.facade.dto.request.WxNotifyRequestDto;
import com.panli.pay.facade.dto.request.WxV2NotifyRequestDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1")
@Api(value = "this is pay controller")
public class NotifyController {
    @Autowired
    private NotifyFacade notifyFacade;

    @PostMapping(value = "/notify/wx")
    public ResourceResponse handlerPaymentForWx(@RequestBody WxNotifyRequestDto dto, HttpServletRequest req) {
        notifyFacade.handlerForWx(dto, req);
        return ResourceResponse.ok();
    }

    @PostMapping(value = "/notify/wx-v2")
    public ResourceResponse handlerRefundForWxV2(@RequestBody WxV2NotifyRequestDto dto, HttpServletRequest req) {
        notifyFacade.handlerRefundForWxV2(dto, req);
        return ResourceResponse.ok();
    }


    @PostMapping(value = "/notify/ali")
    public ResourceResponse handlerNotifyForAli(HttpServletRequest req) {
        notifyFacade.handlerNotifyForAli(req);
        return ResourceResponse.ok();
    }
}
