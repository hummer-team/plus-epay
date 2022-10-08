package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.BigDecimalUtil;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author edz
 */
@Slf4j
public abstract class BaseWxPayment {

    public int convertAmount(BigDecimal amount) {
        return BigDecimalUtil.mulOf2HalfDown(amount, BigDecimal.valueOf(100)).intValue();
    }

    public String getNotifyUrl(String extendParameter) {
        if (StringUtils.isEmpty(extendParameter)) {
            return null;
        }
        return JSON.parseObject(extendParameter).getString("notifyUrl");
    }

    public abstract PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap);

    public abstract boolean successOfOption(Map<String, Object> resultMap);
}
