package com.panli.pay.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.ProfitSharingOrderCreateRequestDto;
import com.panli.pay.facade.dto.request.ProfitSharingOrderQueryRequestDto;
import com.panli.pay.facade.dto.request.ProfitSharingRateReqDto;
import com.panli.pay.facade.dto.request.ProfitSharingReturnReqDto;
import com.panli.pay.facade.dto.request.ProfitSharingUnfreezeReqDto;
import com.panli.pay.facade.dto.request.ServiceMerchantAddReceiverReqDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.facade.dto.response.ProfitSharingRateQueryRespDto;
import com.panli.pay.support.model.bo.payment.WxServiceMerchantAddReceiverRespDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author edz
 */
@RestController
@RequestMapping("/v1")
@Api("this is ProfitSharing controller")
@Validated
public class ProfitSharingController {
    @Autowired
    private PaymentFacade paymentFacade;

    @PostMapping("/profit-sharing/create")
    @ApiOperation("ali,weixin ProfitSharing")
    public ResourceResponse<BasePaymentResp<? extends BasePaymentResp<?>>> createProfitSharing(
            @RequestBody @Valid ProfitSharingOrderCreateRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);

        return ResourceResponse.ok(paymentFacade.createProfitSharing(dto));
    }

    @GetMapping("/profit-sharing/query")
    @ApiOperation("ali,weixin ProfitSharing query")
    public ResourceResponse<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> query(
            @RequestBody @Valid ProfitSharingOrderQueryRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        return ResourceResponse.ok();
    }

    @PostMapping("/profit-sharing/refund")
    @ApiOperation("ali,weixin ProfitSharing query")
    public ResourceResponse<String> refund(
            @RequestBody @Valid ProfitSharingReturnReqDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        return ResourceResponse.ok(paymentFacade.returnProfitSharing(dto));
    }

    @PostMapping("/profitsharing/receivers/add")
    @ApiOperation("ali,weixin service merchant profitsharing receivers add")
    public ResourceResponse<WxServiceMerchantAddReceiverRespDto> addReceivers(
            @RequestBody @Valid ServiceMerchantAddReceiverReqDto dto, Errors errors) {

        return ResourceResponse.ok(paymentFacade.addReceivers(dto));
    }

    @PostMapping("/profitsharing/rate/query")
    @ApiOperation("ali,weixin service merchant profitsharing rate query")
    public ResourceResponse<ProfitSharingRateQueryRespDto> queryRate(
            @RequestBody @Valid ProfitSharingRateReqDto reqDto, Errors errors) {

        return ResourceResponse.ok(paymentFacade.queryProfitSharingRate(reqDto));
    }

    @PostMapping("/profitsharing/unfreeze")
    @ApiOperation("ali,weixin service merchant profitsharing unfreeze")
    public ResourceResponse<Void> queryRate(
            @RequestBody @Valid ProfitSharingUnfreezeReqDto reqDto, Errors errors) {
        paymentFacade.unfreezeProfitSharingOrder(reqDto);
        return ResourceResponse.ok();
    }
}
