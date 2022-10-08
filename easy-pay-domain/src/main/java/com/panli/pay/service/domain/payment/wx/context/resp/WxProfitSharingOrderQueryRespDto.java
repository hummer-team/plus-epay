package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;


/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxProfitSharingOrderQueryRespDto extends BasePaymentQueryResp<WxProfitSharingOrderQueryRespDto> {
    private String state;
    private String subMchId;
    private String transactionId;
    private String outOrderNo;
    private String orderId;

    private List<Receivers> receivers;

    @Data
    public static class Receivers {
        private Integer amount;
        private String description;
        private String type;
        private String account;
        private String result;
        private String detailId;
        private String failReason;
        private Date createTime;
        private Date finishTime;
    }
}
