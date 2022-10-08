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
public class WxProfitSharingReturnReqDto extends BasePaymentChannelReqBodyContext {

    @JSONField(name = "sub_mchid")
    private String subMchId;
    @JSONField(name = "return_mchid")
    private String returnMchId;
    @JSONField(name = "amount")
    private Integer amount;
    @JSONField(name = "description")
    private String remark;
    @JSONField(name = "out_order_no")
    private String profitSharingTradeId;
    @JSONField(name = "order_id")
    private String channelPsId;
    @JSONField(name = "out_return_no")
    private String returnId;


    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }
}
