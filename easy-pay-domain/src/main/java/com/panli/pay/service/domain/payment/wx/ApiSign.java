package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.support.model.po.ChannelCertPo;
import org.apache.http.Header;

public interface ApiSign {
    Header signOfHeader(String mchid, String httpMethod, String url, String body, ChannelCertPo certPo) throws Exception;

    Header signOfHeader(ChannelCertPo certPo) throws Exception;
}
