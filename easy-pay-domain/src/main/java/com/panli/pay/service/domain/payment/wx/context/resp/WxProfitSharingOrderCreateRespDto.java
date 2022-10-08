package com.panli.pay.service.domain.payment.wx.context.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author edz
 */
@Data
public class WxProfitSharingOrderCreateRespDto {
    @JSONField(name = "sub_mchid")
    private String subMchId;
    @JSONField(name = "transaction_id")
    private String transactionId;
    @JSONField(name = "out_order_no")
    private String outOrderNo;
    @JSONField(name = "order_id")
    private String orderId;
    private String state;
    private List<Receivers> receivers;

    @Data
    public static class Receivers {
        private String type;
        private String account;
        private Integer amount;
        private String description;
        private String result;
        @JSONField(name = "fail_reason")
        private String failReason;
        @JSONField(name = "detail_id")
        private String detailId;
        @JSONField(name = "create_time")
        private Date createTime;
        @JSONField(name = "finish_time")
        private Date finishTime;
    }
}
