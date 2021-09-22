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

package com.panli.pay.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

/**
 * @author edz
 */
@Data
public class SimpleDemoSaveReqDto {
    @NotEmpty(message = "third Party Delivery value can't empty.")
    @ApiModelProperty(required = true)
    private String thirdPartyDeliveryId;

    @NotEmpty(message = "third Party Delivery code value can't empty.")
    @ApiModelProperty(required = true)
    @Length(max = 100,message = "max length is 100 char")
    private String deliveryCompanyCode;

    @NotEmpty(message = "batch id can't empty.")
    @Length(max = 200, message = "max length is 200 char")
    private String batchId;
    private String buyerId;
    private String createdUserId;
    @ApiModelProperty(notes = "物流类型，0:国内，1:番丽，2:国际")
    @Range(max = 2, min = 1, message = "valid value is 0 or 1 or 2")
    private Integer deliveryType;
    private String deliveryTypeDescribe;
}
