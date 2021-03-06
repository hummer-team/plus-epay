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

package com.panli.pay.service.domain.enums;

import org.apache.commons.lang3.StringUtils;

public enum PayChannelTypeEnum {
    WEI_XIN(0),
    ALI_PAY(1),
    YUG_NEAN(2),
    CASH(9),
    POST_OFFICE(7);

    private int code;

    PayChannelTypeEnum(int code) {
        this.code = code;
    }

    public static PayChannelTypeEnum getByChannelCode(String channelCode) {
        if (StringUtils.startsWithIgnoreCase(channelCode, "wx")) {
            return WEI_XIN;
        }

        if (StringUtils.startsWithIgnoreCase(channelCode, "ali")) {
            return ALI_PAY;
        }

        if (StringUtils.startsWithIgnoreCase(channelCode, "yug")) {
            return YUG_NEAN;
        }

        throw new IllegalArgumentException("invalid channel code " + channelCode);
    }

    public int getCode() {
        return code;
    }
}
