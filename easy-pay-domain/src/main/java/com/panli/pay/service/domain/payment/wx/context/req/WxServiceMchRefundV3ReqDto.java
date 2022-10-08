package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chenwei
 */
@Data
public class WxServiceMchRefundV3ReqDto extends WxRefundV3ReqDto {

    @JSONField(name = "sub_mchid")
    private String subMchId;
}
