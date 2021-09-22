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

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author edz
 */
@Data
public class BasePaymentRequestDto {
    @NotEmpty(message = "platform code can not empty or null.")
    private String platformCode;
    @NotEmpty(message = "trade can not empty or null.")
    private String tradeId;
    @NotNull(message = "pay message can not null.")
    private Pay pays;
    @NotEmpty(message = "userId can not empty or null.")
    private String userId;
    @NotEmpty(message = "platformSubType can not empty,valid value for example:MD")
    private String platformSubType;
    private Integer orderTag = 0;
    private String merchantId;
    @NotEmpty(message = "payment remark can not empty")
    private String remark;

    @Data
    @Valid
    public static class Pay {
        /**
         * payment short code
         */
        @NotEmpty(message = "channel code can not empty or null.")
        private String channelCode;
        private BigDecimal amount;
        private Map<String, Object> parameter;
    }
}
