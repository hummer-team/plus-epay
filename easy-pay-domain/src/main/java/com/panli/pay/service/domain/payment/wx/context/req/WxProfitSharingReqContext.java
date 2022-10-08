package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.panli.pay.service.domain.context.BasePaymentChannelReqBodyContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ProfitSharing request context
 *
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxProfitSharingReqContext extends BasePaymentChannelReqBodyContext {
    @JSONField(name = "merchantId")
    private String merchantId;

    @JSONField(name = "sub_mchid")
    private String subMchId;

    @JSONField(name = "appid")
    private String appId;
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "out_order_no")
    private String outOrderNo;

    @JSONField(name = "unfreeze_unsplit")
    private Boolean unfreezeUnSplit;

    private List<Receivers> receivers;

    @Override
    public String requestBody() {
        return JSON.toJSONString(this);
    }

    @Data
    public static class Receivers {
        private String type;
        private String account;
        private Integer amount;
        private String description;
    }
}
