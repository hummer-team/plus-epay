package com.panli.pay.service.domain.services;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.CollectionUtil;
import com.hummer.common.utils.DateUtil;
import com.panli.pay.dao.ChannelCertDao;
import com.panli.pay.dao.ChannelConfigDao;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.integration.wxpayment.response.CertificatResponseDto;
import com.panli.pay.service.domain.enums.ChannelPlatformEnum;
import com.panli.pay.service.domain.payment.wx.ApiSign;
import com.panli.pay.service.domain.payment.wx.WxEncodeUtils;
import com.panli.pay.support.model.po.ChannelCertPo;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@Slf4j
public class WxCertificateServiceImpl implements CertificateService {
    @Autowired
    private ChannelConfigDao channelConfigDao;
    @Autowired
    private ChannelCertDao channelCertDao;
    @Autowired
    private ApiSign apiSign;
    @Autowired
    private WxEncodeUtils wxEncodeUtils;

    @Override
    public List<CertificatResponseDto> downloadCertificate(String channelCode) throws Exception {
        ChannelConfigPo config = channelConfigDao.queryByCode(channelCode);
        if (config == null) {
            throw new AppException(40000, "channelCode invalid");
        }
        ChannelCertPo certPo = channelCertDao.selectByPrimaryKey(ChannelPlatformEnum.WX.name(), config.getMerchantId());
        if (certPo == null) {
            throw new AppException(40000, "channel cert config invalid");
        }
        String content = downloadWxCert(certPo);
        // 解密获取
        List<CertificatResponseDto> certs = parseCertContent(content, config.getPrivateKey());
        // 更新
        updateChannelCert(certPo, certs);
        return certs;
    }

    private void updateChannelCert(ChannelCertPo certPo, List<CertificatResponseDto> certs) {
        if (CollectionUtil.isEmpty(certs)) {
            return;
        }
        CertificatResponseDto cert = certs.get(0);
        if (cert == null) {
            return;
        }
        ChannelCertPo po = new ChannelCertPo();
        po.setId(certPo.getId());
        po.setLastModifiedDateTime(DateUtil.now());
        po.setCertPlatformPri(cert.getCert());
        po.setCertExpireTime(cert.getExpireTime());
        po.setCertPlatformSerialNo(cert.getSerialNo());
        channelCertDao.updateById(po);
    }

    private List<CertificatResponseDto> parseCertContent(String content, String key) throws GeneralSecurityException, IOException {
        List<CertificatResponseDto> certs = JSON.parseArray(
                JSON.parseObject(content).getString("data"), CertificatResponseDto.class);
        //最新时间
        for (CertificatResponseDto cert : certs) {
            CertificatResponseDto.EncryptCertificate encrypt = cert.getEncryptCertificate();
            String s = wxEncodeUtils.decryptToString(key.getBytes(), encrypt.getAssociatedData().getBytes()
                    , encrypt.getNonce().getBytes(), encrypt.getCiphertext());
            cert.setCert(s);
        }
        return certs;
    }

    private String downloadWxCert(ChannelCertPo cert) throws Exception {

        String url = cert.getTokenApiUrl();
        String s = WxPayClient.doGet(url
                , 3000
                , 1
                , apiSign.signOfHeader(cert)
                , new BasicHeader("Accept", "application/json"));
        log.info("download wx certificate result {} ", s);
        return s;
    }

}
