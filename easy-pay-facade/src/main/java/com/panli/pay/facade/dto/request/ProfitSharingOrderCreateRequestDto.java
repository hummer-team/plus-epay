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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ed
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账，微信：https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml，支付宝：")
public class ProfitSharingOrderCreateRequestDto extends BaseProfitSharingOrderRequestDto<ProfitSharingOrderCreateRequestDto> {
    @NotNull(message = "ProfitSharing Receivers can not null")
    private List<Receivers> receivers;
    private Boolean unfreezeUnSplit = true;

    @ApiModelProperty(name = "分账订单交易ID")
    @NotEmpty(message = "ProfitSharing trade id can not null")
    private String fzTradeId;

    @Data
    @Valid
    public static class Receivers {
        @NotEmpty(message = "type can not null")
        private String type;
        @NotEmpty(message = "account id can not null")
        private String account;
        private String realName;
        @NotNull(message = "amount value can not null")
        private Integer amount;
        @NotEmpty(message = "memo  can not null")
        private String description;
    }
}
