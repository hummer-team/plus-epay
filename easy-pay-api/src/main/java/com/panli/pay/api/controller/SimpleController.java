package com.panli.pay.api.controller;

import com.hummer.core.SpringApplicationContext;
import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.panli.pay.facade.SimpleDemoFacade;
import com.panli.pay.facade.dto.request.SimpleDemoSaveReqDto;
import com.panli.pay.integration.SimpleOtherService;
import com.panli.pay.service.domain.event.PaymentInProcessEvent;
import com.panli.pay.service.domain.event.bo.PaymentInProcessBo;
import comm.hummer.simple.common.module.SimpleDubboDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * Delivery Line enter
 */
@RestController
@RequestMapping(value = "/v1")
@Api(value = "this simple controller demo for learning")
@Validated
public class SimpleController {
    @Autowired(required = false)
    private SimpleDemoFacade simpleDemoFacade;
    @Autowired(required = false)
    private SimpleOtherService otherService;

    @PostMapping(value = "/simple/save")
    @ApiOperation(value = "this is save batch info to db demo")
    public ResourceResponse save(@RequestBody @Valid SimpleDemoSaveReqDto reqDto
            , Errors errors) {
        ParameterAssertUtil.assertRequestFirstValidated(errors);
        simpleDemoFacade.save(reqDto);
        return ResourceResponse.ok();
    }

    @GetMapping(value = "/simple/query-single")
    @ApiOperation(value = "this is query demo")
    public ResourceResponse save(@RequestParam("id")
                                 @NotEmpty(message = "id can't null")
                                 @Length(max = 100, message = "max length 100 char.")
                                 String id) {
        return ResourceResponse.ok(simpleDemoFacade.querySingleById(id));
    }

    @GetMapping("/add/{a}/{b}")
    @ApiOperation(value = "this is add demo")
    public ResourceResponse<Integer> add(@PathVariable("a") @Valid @Min(value = 0, message = "min 0") int a
            , @PathVariable("b") @Valid @Min(value = 0, message = "min 0") int b) {
        return ResourceResponse.ok(otherService.add(a, b));
    }

    @GetMapping("/add2/{a}/{b}")
    @ApiOperation(value = "this is add demo")
    public ResourceResponse<Integer> add2(@PathVariable("a") @Valid @Min(value = 0, message = "min 0") Integer a
            , @PathVariable("b") @Valid @Min(value = 0, message = "min 0") Integer b) {
        SimpleDubboDto dto = new SimpleDubboDto();
        dto.setA(a);
        dto.setB(b);
        return ResourceResponse.ok(otherService.add2(dto));
    }

    @PostMapping("/pay/in-process")
    @ApiOperation(value = "pay in-process")
    public ResourceResponse<Void> process(
            @RequestBody @Valid PaymentInProcessBo bo
            ) {

        SpringApplicationContext.publishEvent(new PaymentInProcessEvent(SimpleController.class,bo));
        return ResourceResponse.ok();
    }
}
