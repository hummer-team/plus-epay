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
