package com.panli.pay.support.model.po;

import lombok.Data;

/**
 * @author edz
 */
@Data
public class SimpleDemoPo extends BasePo {
    private String thirdPartyDeliveryId;
    private String deliveryCompanyCode;
    private String batchId;
    private String buyerId;
    private Integer deliveryType;
    private String deliveryTypeDescribe;
    private Integer callApiTimeoutMillisecond;
    private String deliveryInfoChannel;
    private String proxyExpressCompanyCode;
}