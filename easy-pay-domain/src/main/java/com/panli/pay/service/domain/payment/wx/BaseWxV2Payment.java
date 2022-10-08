package com.panli.pay.service.domain.payment.wx;

import java.util.Map;

public abstract class BaseWxV2Payment extends BaseWxPayment {
    /**
     * check barcode payment result is success.
     *
     * @param resultMap resultMap
     */
    @Override
    public boolean successOfOption(Map<String, Object> resultMap) {

        Object returnCode = resultMap.get("return_code");
        Object resultCode = resultMap.get("result_code");

        return "SUCCESS".equals(resultCode)
                && "SUCCESS".equals(returnCode);
    }

}
