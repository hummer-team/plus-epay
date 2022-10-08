package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ProfitSharing request context
 *
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxProfitSharingRateQueryReqDto extends BasePaymentChannelReqBodyContext {

    @JSONField(name = "sub_mchid")
    private String subMchId;

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
