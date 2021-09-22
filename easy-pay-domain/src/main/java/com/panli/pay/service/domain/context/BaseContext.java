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

import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author edz
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class BaseContext<T extends BaseContext<T>> {
    /**
     * data
     */
    private transient T context;
    private String requestId;
    private String userId;
    private String tradeId;

    private String platformCode;
    private String platformSubType;
    private String channelCode;

    private PaymentOrderPo paymentOrder;
    private ChannelConfigPo channelConfigPo;

    /**
     * extra parameter,ensure instance can not null
     */
    private Map<String, Object> affixData = new ConcurrentHashMap<>(4);

    @Override
    public String toString() {
        return
                "requestId=" + requestId +
                        ", platformCode=" + platformCode +
                        ", platformSubType=" + platformSubType +
                        ", parameter=" + affixData +
                        ", channelCode=" + channelCode;
    }
}
