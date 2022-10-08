package com.panli.pay.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.panli.pay.facade.PaymentFacade;
import com.panli.pay.facade.dto.request.ServiceMerchantBarCodePayCancelRequestDto;
import com.panli.pay.facade.dto.request.ServiceMerchantBarCodePayCreateRequestDto;
import com.panli.pay.facade.dto.request.ServiceMerchantBarCodePayQueryRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author edz
 */
@RestController
@RequestMapping(value = "/v1")
@Api(value = "this is service merchant pay controller")
@Validated
public class ServiceMerchantPayController {
    @Autowired
    private PaymentFacade paymentFacade;

    @PostMapping("/service-merchant/pay")
    @ApiOperation(value = "ali,weixin service merchant pay")
    public ResourceResponse<BasePaymentResp<? extends BasePaymentResp<?>>> payment(
            @RequestBody @Valid ServiceMerchantBarCodePayCreateRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);

        Assert.notNull(dto.getPays(), "pay info can not null");
        Assert.notNull(dto.getPays().getParameter(), "pay parameter info can not null");

        // TODO: 2021/9/17 refactor domain object
        dto.getPays().getParameter().put("subAppId", dto.getSubAppId());
        dto.getPays().getParameter().put("subMchId", dto.getSubMchId());
        dto.getPays().getParameter().put("profitSharing", dto.getProfitSharing());

        return ResourceResponse.ok(paymentFacade.payment(dto));
    }

    @PostMapping("/service-merchant/cancel")
    @ApiOperation(value = "ali,weixin service merchant pay")
    public ResourceResponse cancle(
            @RequestBody @Valid ServiceMerchantBarCodePayCancelRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);

        return ResourceResponse.ok(paymentFacade.cancel(dto));
    }

    @PostMapping("/service-merchant/pay/status/query")
    @ApiOperation(value = "ali,weixin service merchant pay status query")
    public ResourceResponse<List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>>> query(
            @RequestBody @Valid ServiceMerchantBarCodePayQueryRequestDto dto, Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        dto.setData(dto);
        return ResourceResponse.ok(paymentFacade.queryPayment(dto));
    }

}
