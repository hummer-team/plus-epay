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
public class WxProfitSharingUnfreezeReqDto extends BasePaymentChannelReqBodyContext {

    @JSONField(name = "sub_mchid")
    private String subMchId;
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "out_order_no")
    private String outOrderNo;
    @JSONField(name = "description")
    private String description;

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
