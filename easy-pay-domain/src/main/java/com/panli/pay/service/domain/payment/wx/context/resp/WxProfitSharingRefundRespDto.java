package com.panli.pay.service.domain.payment.wx.context.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxProfitSharingRefundRespDto {
    @JSONField(name = "sub_mchid")
    private String subMchId;
    @JSONField(name = "order_id")
    private String orderId;
    @JSONField(name = "out_order_no")
    private String outOrderNo;
    @JSONField(name = "out_return_no")
    private String outReturnNo;
    @JSONField(name = "return_id")
    private String returnId;
    @JSONField(name = "return_mchid")
    private String returnMchId;
    @JSONField(name = "amount")
    private Integer amount;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "result")
    private String result;
    @JSONField(name = "fail_reason")
    private String failReason;
    @JSONField(name = "create_time")
    private String createTime;
    @JSONField(name = "finish_time")
    private String finishTime;
}
