package com.panli.pay.api.controller;


import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.BasePaymentCancelRequestDto;
import com.panli.pay.facade.dto.request.BasePaymentRequestDto;
import com.panli.pay.facade.dto.request.RefundRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lee
 */
@RestController
@RequestMapping(value = "/v1")
@Api(value = "this is basic pay controller")
@Validated
public class BasePaymentController {
    @Autowired
    private PaymentFacade paymentFacade;

    @PostMapping(value = "/pay")
    @ApiOperation(value = "ali,weixin pay")
    public ResourceResponse<BasePaymentResp<? extends BasePaymentResp<?>>> payment(
            @RequestBody @Valid BasePaymentRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        return ResourceResponse.ok(paymentFacade.payment(dto));
    }

    @GetMapping(value = {"/payment/status/query", "pay/status/query"})
    @ApiOperation("ali,weixin payment query")
    public ResourceResponse<List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>>> queryPaymentStatusByTradeId(
            @RequestParam("tradeId")
            @NotEmpty(message = "trade id can not empty") String tradeId,
            @RequestParam(value = "platformCode", required = false) String platformCode) {
        return ResourceResponse.ok(paymentFacade.queryPaymentByTradeId(tradeId, platformCode));
    }

    @PostMapping(value = "/refund")
    public ResourceResponse<String> refund(@RequestBody @Valid RefundRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        return ResourceResponse.ok(paymentFacade.refund(dto));
    }

    @PostMapping(value = "/cancel")
    @ApiOperation(value = "ali,weixin cancel")
    public ResourceResponse cancel(@RequestBody @Valid BasePaymentCancelRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        paymentFacade.cancel(dto);
        return ResourceResponse.ok();
    }
}
