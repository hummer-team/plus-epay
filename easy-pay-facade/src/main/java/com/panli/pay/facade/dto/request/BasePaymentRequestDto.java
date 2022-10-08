package com.panli.pay.facade.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author edz
 */
@Data
public class BasePaymentRequestDto {
    @NotEmpty(message = "platform code can not empty or null.")
    private String platformCode;
    @NotEmpty(message = "trade can not empty or null.")
    private String tradeId;
    @NotNull(message = "pay message can not null.")
    private Pay pays;
    @NotEmpty(message = "userId can not empty or null.")
    private String userId;
    @NotEmpty(message = "platformSubType can not empty,valid value for example:MD")
    private String platformSubType;
    private Integer orderTag = 0;
    @NotEmpty(message = "payment remark can not empty")
    private String remark;

    private Date paymentTimeOut;

    @Data
    @Valid
    public static class Pay {
        /**
         * payment short code
         */
        @NotEmpty(message = "channel code can not empty or null.")
        private String channelCode;
        private BigDecimal amount;
        private Map<String, Object> parameter;
    }
}
