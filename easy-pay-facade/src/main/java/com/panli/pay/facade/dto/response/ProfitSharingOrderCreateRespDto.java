package com.panli.pay.facade.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author edz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfitSharingOrderCreateRespDto extends BasePaymentResp<ProfitSharingOrderCreateRespDto> {
    private String channelFzOrderId;
    private String platformFzOrderId;
    private String subMchId;

    private List<Receivers> receivers;

    @Data
    public static class Receivers {
        private String type;
        private String account;
        private Integer amount;
        private String description;
        private String result;
        private String failReason;
        private String detailId;
        private Date createTime;
        private Date finishTime;
    }
}
