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

import com.hummer.common.exceptions.AppException;
import com.panli.pay.support.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.panli.pay.integration.wxpayment.WXPayConstant.KEY;

@Service
@Slf4j
public class WxResponseServiceImpl implements WxResponseService {
    private Map<String, X509Certificate> certificateMap;
    @Autowired
    private CertificateService certificateService;

    @PostConstruct
    private void init() throws Exception {
        //certificateMap = certificateService.downloadCertificate("wxJsapiPayment");
    }

    @Override
    public boolean verify(HttpServletRequest request, String jsonBody) {
        //??????????????????????????????
        String serialNo = request.getHeader("Wechatpay-Serial");
        //??????????????????????????????
        String nonceStr = request.getHeader("Wechatpay-Nonce");
        //????????????????????????
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        //?????????????????????
        String wechatSign = request.getHeader("Wechatpay-Signature");
        //?????????????????????
        String signStr = Stream.of(timestamp, nonceStr, jsonBody)
                .collect(Collectors.joining("\n", "", "\n"));

        //?????????????????????????????????
        X509Certificate certificate = certificateMap.get(serialNo);
        //???????????? ????????????
        if (certificate == null) {
            log.warn("serialNo not found certificate,verify failed");
            return false;
        }
        //SHA256withRSA sign
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(certificate);
            signature.update(signStr.getBytes());
            //??????????????????
            return signature.verify(Base64Utils.decodeFromString(wechatSign));
        } catch (Throwable e) {
            log.error("wx response content verify failed,{} ->", jsonBody, e);
            return false;
        }
    }

    @Override
    public String tryDecryptBody(String body, String nonce, String ciphertext) {
        try {
            return new AesUtil(KEY).decryptToString(body.getBytes(), nonce.getBytes(), ciphertext);
        } catch (Throwable e) {
            log.error("wx response content decryptToString failed,{} - {} - {} ->"
                    , body, nonce, ciphertext);
            throw new AppException(50000, "wx response content decryptToString failed");
        }
    }
}
