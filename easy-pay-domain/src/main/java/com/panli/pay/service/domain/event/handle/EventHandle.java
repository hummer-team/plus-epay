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

package com.panli.pay.service.domain.event.handle;

import com.hummer.common.exceptions.SysException;
import com.panli.pay.dao.SysLogDao;
import com.panli.pay.service.domain.event.BaseEvent;
import com.panli.pay.service.domain.event.SysLogEvent;
import com.panli.pay.support.model.po.SysLogPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Slf4j
public class EventHandle {

    @Autowired
    private SysLogDao sysLogDao;

    @EventListener
    public void handle(@NotNull BaseEvent event) {
        log.debug("handle vent", event);
    }

    @EventListener
    public void handlerSysLogEvent(@NotNull SysLogEvent event) {
        SysLogPo po = new SysLogPo();
        po.setBusinessCode(event.getPlatformSubType());
        po.setChannelCode(event.getChannelCode());
        po.setPlatformCode(event.getPlatformCode());
        po.setRequestBody(event.getRequestBody());
        po.setRequestId(event.getRequestId());
        Throwable e = event.getE();
        if (e != null) {
            if (e instanceof SysException) {
                SysException sysE = (SysException) e;
                po.setSysCode(sysE.getCode());
                po.setSysMessage(sysE.toString());
            } else {
                po.setSysCode(50000);
                po.setSysMessage(e.toString());
            }
        }
        po.setCreatedUserId(event.getUserId());

        sysLogDao.insert(po);
    }
}