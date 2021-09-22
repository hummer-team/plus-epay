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

import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author edz
 */
@Data
public class BaseProfitSharingOrderRequestDto<T extends BaseProfitSharingOrderRequestDto<T>> {
    @NotEmpty(message = "sub mch id can not null")
    private String subMchId;
    @NotEmpty(message = "sub mch appId can not null")
    private String subAppId;
    private String platformCode;
    @NotEmpty(message = "channelCode can not null")
    private String channelCode;
    @NotEmpty(message = "trade id can not empty")
    private String tradeId;
    private String userId;

    @ApiModelProperty(hidden = true)
    private transient T data;

    private Map<String, Object> affixData = new ConcurrentHashMap<>();
}
