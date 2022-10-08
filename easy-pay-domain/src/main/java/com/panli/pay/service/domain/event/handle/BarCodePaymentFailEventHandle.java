package com.panli.pay.service.domain.event.handle;

import com.alibaba.fastjson.JSONObject;
import com.hummer.common.SysConstant;
import com.hummer.delay.queue.plugin.holder.DelayQueueManager;
import com.panli.pay.service.domain.core.delay.bo.PaymentInProcessDelayBo;
import com.panli.pay.service.domain.core.delay.contants.DelayConstants;
import com.panli.pay.service.domain.event.PaymentInProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * AfterSaleFailEventHandle
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/2/26 15:18
 */
@Component
public class BarCodePaymentFailEventHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarCodePaymentFailEventHandle.class);

    @Autowired
    private DelayQueueManager delayQueueManager;

    @EventListener
    public void handle(@NotNull PaymentInProcessEvent event) {
        if (event.getFailBo() == null) {
            return;
        }
        LOGGER.info("barcode payment in process handling start,bo=={}", JSONObject.toJSONString(event.getFailBo()));
        if (event.getFailBo().getTradeId() == null) {
            return;
        }
        // 加入延迟队列
        PaymentInProcessDelayBo info = new PaymentInProcessDelayBo();
        info.setParams(event.getFailBo());
        info.setExpireTime(System.currentTimeMillis() + 2000);
        info.setRequestId(MDC.get(SysConstant.REQUEST_ID));
        info.setHandlerName(DelayConstants.ConsumerName.PAYMENT_IN_PROCESS_CONSUMER);
        info.setRetryCount(0);
        delayQueueManager.push(info);
    }

}
