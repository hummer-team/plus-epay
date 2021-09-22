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

import com.panli.pay.facade.SimpleDemoFacade;
import com.panli.pay.facade.dto.request.SimpleDemoSaveReqDto;
import com.panli.pay.integration.SimpleOtherService;
import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.utils.ParameterAssertUtil;
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
}
