package com.panli.pay.service.domain.event;

import com.panli.pay.service.domain.event.bo.PaymentInProcessBo;
import org.springframework.context.ApplicationEvent;

/**
 * BarCodePaymentFailEvent
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/3/8 11:13
 */
public class PaymentInProcessEvent extends ApplicationEvent {

    private PaymentInProcessBo failBo;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public PaymentInProcessEvent(Object source, PaymentInProcessBo failBo) {
        super(source);
        this.failBo = failBo;
    }

    public PaymentInProcessBo getFailBo() {
        return failBo;
    }
}
