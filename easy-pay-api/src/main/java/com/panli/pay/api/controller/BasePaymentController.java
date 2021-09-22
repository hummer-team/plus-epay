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
import com.hummer.rest.utils.ParameterAssertUtil;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.BasePaymentCancelRequestDto;
import com.panli.pay.facade.dto.request.BasePaymentRequestDto;
import com.panli.pay.facade.dto.request.RefundRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.facade.dto.response.QueryPaymentStatusCommonRespDto;
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
    public ResourceResponse refund(@RequestBody @Valid RefundRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        paymentFacade.refund(dto);
        return ResourceResponse.ok();
    }

    @PostMapping(value = "/cancel")
    @ApiOperation(value = "ali,weixin cancel")
    public ResourceResponse cancel(@RequestBody @Valid BasePaymentCancelRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        paymentFacade.cancel(dto);
        return ResourceResponse.ok();
    }
}
