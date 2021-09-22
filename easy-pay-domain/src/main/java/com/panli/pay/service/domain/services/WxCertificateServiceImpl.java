/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.panli.pay.service.domain.services;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.http.HttpResult;
import com.panli.pay.dao.ChannelConfigDao;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.integration.wxpayment.response.CertificatResponseDto;
import com.panli.pay.service.domain.payment.wx.ApiSign;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.panli.pay.integration.wxpayment.WXPayConstant.KEY;

@Service
@Slf4j
public class WxCertificateServiceImpl implements CertificateService {
    @Autowired
    private ChannelConfigDao channelConfigDao;
    @Autowired
    private ApiSign apiSign;

    @Override
    public Map<String, X509Certificate> downloadCertificate(String channelCode) throws Exception {
        String content = downloadWxCert(channelCode);
        //保存微信平台证书公钥
        Map<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();

        List<CertificatResponseDto> certificateList = JSON.parseArray(
                JSON.parseObject(content).getString("data"), CertificatResponseDto.class);
        //最新时间
        for (CertificatResponseDto cert : certificateList) {
            CertificatResponseDto.EncryptCertificate encryptCertificate = cert.getEncryptCertificate();
            //获取证书字公钥
            String publicKey = new AesUtil(KEY).decryptToString(
                    encryptCertificate.getAssociatedData().replaceAll("\"", "").getBytes()
                    , encryptCertificate.getNonce().replaceAll("\"", "").getBytes()
                    , encryptCertificate.getCiphertext().replaceAll("\"", ""));
            CertificateFactory cf = CertificateFactory.getInstance("X509");

            //获取证书
            ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKey.getBytes(StandardCharsets.UTF_8));
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(inputStream);

            certificateMap.put(cert.getSerialNo(), certificate);
        }
        return certificateMap;
    }

    private String downloadWxCert(String channelCode) throws Exception {
        ChannelConfigPo channelConfigPo = channelConfigDao.queryByCode(channelCode);
        if (channelConfigPo == null) {
            throw new AppException(40000, "channelCode invalid");
        }
        String url = "https://api.mch.weixin.qq.com/v3/certificates";
        HttpResult httpResult = WxPayClient.doGet(url
                , 3000
                , 1
                , apiSign.signOfHeader(channelConfigPo.getMerchantId(), "GET", url, null)
                , new BasicHeader("Accept", "application/json"));
        log.info("download wx certificate result {} - {}", httpResult, httpResult.getAllHeader());

        return httpResult.getResult();
    }
}
