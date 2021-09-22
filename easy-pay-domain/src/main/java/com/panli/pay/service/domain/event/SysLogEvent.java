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

package com.panli.pay.service.domain.event;

import org.slf4j.MDC;
import org.springframework.context.ApplicationEvent;

import static com.hummer.common.SysConstant.REQUEST_ID;

public class SysLogEvent extends ApplicationEvent {

    private String platformCode;
    private String platformSubType;
    private String channelCode;
    private String requestId;
    private String requestBody;
    private String userId;

    private Throwable e;

    public SysLogEvent(Object source
            , String platformCode
            , String platformSubType
            , String channelCode
            , String requestBody
            , String userId
            , Throwable e) {
        super(source);
        this.platformCode = platformCode;
        this.platformSubType = platformSubType;
        this.channelCode = channelCode;
        this.requestId = MDC.get(REQUEST_ID);
        this.requestBody = requestBody;
        this.userId = userId;
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public String getPlatformSubType() {
        return platformSubType;
    }

    public String getChannelCode() {
        return channelCode;
    }


    public String getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequestBody() {
        return requestBody;
    }
}
