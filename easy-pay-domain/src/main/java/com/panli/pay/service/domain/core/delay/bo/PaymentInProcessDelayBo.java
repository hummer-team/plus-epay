package com.panli.pay.service.domain.core.delay.bo;

import com.hummer.delay.queue.plugin.model.DelayQueueInfo;
import com.panli.pay.service.domain.event.bo.PaymentInProcessBo;
import lombok.Getter;
import lombok.Setter;

/**
 * PaymentInProcessDelayBo
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/9/24 13:25
 */
@Getter
@Setter
public class PaymentInProcessDelayBo extends DelayQueueInfo<PaymentInProcessDelayBo> {

    private PaymentInProcessBo params;
}
