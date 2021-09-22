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

package com.panli.pay.facade;

import com.panli.pay.facade.dto.request.BasePaymentCancelRequestDto;
import com.panli.pay.facade.dto.request.BasePaymentRequestDto;
import com.panli.pay.facade.dto.request.BaseProfitSharingOrderRequestDto;
import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.facade.dto.request.RefundRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import com.panli.pay.facade.dto.response.BasePaymentResp;

import java.util.List;

public interface PaymentFacade {
    BasePaymentResp<? extends BasePaymentResp<?>> payment(BasePaymentRequestDto dto);

    BasePaymentResp<? extends BasePaymentResp<?>> createProfitSharing(
            BaseProfitSharingOrderRequestDto<? extends BaseProfitSharingOrderRequestDto<?>> dto);

    List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>> queryPaymentByTradeId(String tradeId, String platformCode);

    List<BasePaymentQueryResp<? extends BasePaymentQueryResp<?>>>
    queryPayment(BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto);

    void refund(RefundRequestDto dto);

    void cancel(BasePaymentCancelRequestDto dto);
}
