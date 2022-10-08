package com.panli.pay.service.domain.event.bo;

import lombok.Data;

import java.util.Date;

/**
 * BarCodePaymentFailBo
 *
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/3/8 11:14
 */
@Data
public class PaymentInProcessBo {

    private String tradeId;

    private Date paymentOutTime;

    private String platformCode;

    private String platformSubType;

    private String subAppId;

    private String subMchId;

    private String channelCode;

    private String userId;
}
