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

package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.BigDecimalUtil;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author edz
 */
@Slf4j
public abstract class BaseWxPayment {

    public PaymentStatusEnum handlerPaymentStatus() {
        throw new NotImplementedException();
    }

    public int convertAmount(BigDecimal amount) {
        return amount.multiply(BigDecimalUtil.valueOf("100")).intValue();
    }

    public String getNotifyUrl(String extendParameter) {
        if (StringUtils.isEmpty(extendParameter)) {
            return null;
        }
        return JSON.parseObject(extendParameter).getString("notifyUrl");
    }
}
