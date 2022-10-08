package com.panli.pay.service.domain.context;

import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author edz
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class BaseContext<T extends BaseContext<T>> {
    /**
     * data
     */
    private transient T context;
    private String requestId;
    private String userId;
    private String tradeId;

    private String platformCode;
    private String platformSubType;
    private String channelCode;

    private PaymentOrderPo paymentOrder;
    private ChannelConfigPo channelConfigPo;
    /**
     * extra parameter,ensure instance can not null
     */
    private Map<String, Object> affixData = new ConcurrentHashMap<>(4);

    @Override
    public String toString() {
        return
                "requestId=" + requestId +
                        ", platformCode=" + platformCode +
                        ", platformSubType=" + platformSubType +
                        ", parameter=" + affixData +
                        ", channelCode=" + channelCode;
    }

}
