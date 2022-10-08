package com.panli.pay.service.domain.services;

import com.panli.pay.integration.wxpayment.response.CertificatResponseDto;

import java.util.List;

public interface CertificateService {
    List<CertificatResponseDto> downloadCertificate(String channelCode) throws Exception;
}
