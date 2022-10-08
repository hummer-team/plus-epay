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