package com.panli.pay.service.domain.payment.wx.context.resp;

import com.panli.pay.facade.dto.response.BasePaymentQueryResp;
import lombok.Data;

import java.util.Date;

@Data
public class WxProfitSharingOrderReturnRespDto extends BasePaymentQueryResp<WxProfitSharingOrderReturnRespDto> {
    private String subMchId;
    private String orderId;
    private String outOrderNo;
    private String outReturnNo;
    private String returnId;
    private String returnMchId;
    private String description;
    private String result;
    private String failReason;
    private Date createTime;
    private Date finishTime;
}
