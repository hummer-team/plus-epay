package com.panli.pay.support.model.bo.payment;

import lombok.Data;

/**
 * BasePaymentCancelResp
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/9/26 16:29
 */
@Data
public class BasePaymentCancelResp {

    private boolean isSuccess;

    private boolean recall;

    private String channelRespCode;

    private String channelRespDesc;
}
