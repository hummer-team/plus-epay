package com.panli.pay.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.panli.pay.integration.wxpayment.response.CertificatResponseDto;
import com.panli.pay.service.domain.services.CertificateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lee
 */
@RestController
@RequestMapping(value = "/v1")
@Api(value = "this is pay controller")
public class CertController {
    @Autowired
    private CertificateService certificateService;

    @GetMapping("/wx-cert/update")
    public ResourceResponse<List<CertificatResponseDto>> updateWxCert(
            @RequestParam("channelCode") String channelCode
    ) throws Exception {

        return ResourceResponse.ok(certificateService.downloadCertificate(channelCode));
    }
}
