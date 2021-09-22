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

import com.hummer.common.exceptions.AppException;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * this is epay service inner common result dto for channel weix and alipay
 *
 * @author edz
 */
@Data
@SuperBuilder
public abstract class BaseResultContext<F extends BaseResultContext<F>> {
    private transient F result;

    /**
     * channel response origin string context
     */
    private String channelOriginResponse;
    /**
     * channel response dto object
     */
    private Object channelOriginRespDto;

    private boolean success;
    private int costTimeMills;

    private String channelRespMessage;
    private String channelRespCode;
    private String channelSubCode;
    private String channelSubMessage;

    private String requestId;
    private String platformCode;
    private String channelCode;

    /**
     * parse channelOriginRespDto to R, if is null then  throw {@link IllegalArgumentException}
     *
     * @param <R> R is target class
     * @return R
     * @throws IllegalArgumentException
     */
    public <R> R convertOriginRespDto() {
        R r = (R) channelOriginRespDto;
        if (r == null) {
            throw new IllegalArgumentException("channelOriginRespDto can not convert to R");
        }
        return r;
    }

    public void assertSuccess(String message) {
        if (!success) {
            throw new AppException(50000, String.format("%s - failed - %s"
                    , channelCode, channelRespMessage)
                    , message);
        }
    }
}
