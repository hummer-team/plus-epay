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

package com.panli.pay.service.domain.context;

import com.panli.pay.service.domain.enums.OrderTypeEnum;
import com.panli.pay.service.domain.enums.PayChannelTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class PaymentContext extends BaseContext<PaymentContext> {

    private BigDecimal amount;
    private Integer beanNumber;
    private Integer orderTag;

    private Integer amountUnitType;

    private PayChannelTypeEnum payChannelType;
    private String remark;
    private String terminalIp;

    private String channelTradeId;
    private String batchId;
    private String merchantId;
    private OrderTypeEnum orderType;

    @Override
    public String toString() {
        return "PaymentContext [" +
                "amount=" + amount +
                ", beanNumber=" + beanNumber +
                ", orderTag=" + orderTag +
                ", amountUnitType=" + amountUnitType +
                ", payChannelType=" + payChannelType +
                ", remark=" + remark +
                ", terminalIp=" + terminalIp +
                ", channelTradeId=" + channelTradeId +
                ", batchId=" + batchId +
                ", merchantId=" + merchantId +
                ", super=" + super.toString() +
                ']';
    }
}
