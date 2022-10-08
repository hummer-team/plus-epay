package com.panli.pay.service.domain.result;

import com.hummer.common.exceptions.AppException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * this is epay service inner common result dto for channel weix and alipay
 *
 * @author edz
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseResultContext<F extends BaseResultContext<F>> {
    private transient F result;

    /**
     * channel response origin string context
     */
    private String channelOriginResponse;
    /**
     * channel response dto object
     */
    private Object channelOriginRespDto;

    private boolean success = true;
    private int costTimeMills;

    private String channelRespMessage;
    private String channelRespCode;
    private String channelSubCode;
    private String channelSubMessage;

    private String requestId;
    private String platformCode;
    private String channelCode;

    /**
     * parse channelOriginRespDto to R, if is null then  throw {@link IllegalArgumentException}
     *
     * @param <R> R is target class
     * @return R
     * @throws IllegalArgumentException
     */
    public <R> R convertOriginRespDto() {
        R r = (R) channelOriginRespDto;
        if (r == null) {
            throw new IllegalArgumentException("channelOriginRespDto can not convert to R");
        }
        return r;
    }

    public void assertSuccess(String message) {
        if (!success) {
            throw new AppException(50000, String.format("%s - failed - %s"
                    , channelCode, channelRespMessage)
                    , message);
        }
    }
}
