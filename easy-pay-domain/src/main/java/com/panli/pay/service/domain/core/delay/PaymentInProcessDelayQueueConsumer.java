package com.panli.pay.service.domain.core.delay;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.DateUtil;
import com.hummer.core.PropertiesContainer;
import com.hummer.core.SpringApplicationContext;
import com.hummer.delay.queue.plugin.consumer.DelayQueuePollConsumer;
import com.hummer.delay.queue.plugin.holder.DelayQueueManager;
import com.panli.pay.service.domain.core.delay.bo.PaymentInProcessDelayBo;
import com.panli.pay.service.domain.core.delay.contants.DelayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ShopBarcodePaymentDelayQueueConsumer
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/7/8 14:19
 */
@Component(DelayConstants.ConsumerName.PAYMENT_IN_PROCESS_CONSUMER)
@Slf4j
public class PaymentInProcessDelayQueueConsumer implements DelayQueuePollConsumer<PaymentInProcessDelayBo> {

    @Autowired
    private PaymentInProcessHandler paymentInProcessHandler;

    @Override
    public void handle(PaymentInProcessDelayBo t) {
        log.debug("barcode payment delay do handle:{}", JSON.toJSONString(t));
//        CompletableFuture.runAsync(() -> {
        boolean flag = false;
        try {
            flag = paymentInProcessHandler.handle(t.getParams());
        } catch (Exception e) {
            log.error("payment in process delay handle fail bo-- {}", t, e);
        }
        if (flag) {
            success(t);
        } else {
            fail(t);
        }

//        }, SpringApplicationContext.getBean(Constants.POOL_DEFAULT_TASK_GROUP_V2, Executor.class));
    }

    @Override
    public void success(PaymentInProcessDelayBo t) {

        log.info("barcode payment delay do handle success:{}", JSON.toJSONString(t));
    }

    @Override
    public void fail(PaymentInProcessDelayBo t) {

        if (!confirmPushDelay(t)) {
            log.error("shop order {},query retry {} count,payment not completed"
                    , t.getRetryCount(), JSON.toJSONString(t.getData()));
            return;
        }
        log.warn("shop order {},query retry {} count,payment not completed"
                , t.getRetryCount(), JSON.toJSONString(t.getData()));
        t.setRetryCount(t.getRetryCount() + 1);
        t.setExpireTime(System.currentTimeMillis() + 2000);
        SpringApplicationContext.getBean(DelayQueueManager.class).push(t);
    }

    private boolean confirmPushDelay(PaymentInProcessDelayBo t) {
        Date outTime = t.getParams().getPaymentOutTime();
        if (outTime == null && t.getRetryCount() <= 0) {
            return false;
        }
        if (outTime != null && outTime.after(DateUtil.now())) {
            return true;
        }
        if (t.getRetryCount() > 0) {
            return t.getRetryCount() < PropertiesContainer.valueOfInteger("shop.barcode.payment.delay.retry.max"
                    , 10);
        }
        return false;
    }
}
