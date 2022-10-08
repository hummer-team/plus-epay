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
        //微信返回的证书序列号
        String serialNo = request.getHeader("Wechatpay-Serial");
        //微信返回的随机字符串
        String nonceStr = request.getHeader("Wechatpay-Nonce");
        //微信返回的时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        //微信返回的签名
        String wechatSign = request.getHeader("Wechatpay-Signature");
        //组装签名字符串
        String signStr = Stream.of(timestamp, nonceStr, jsonBody)
                .collect(Collectors.joining("\n", "", "\n"));

        //根据序列号获取平台证书
        X509Certificate certificate = certificateMap.get(serialNo);
        //获取失败 验证失败
        if (certificate == null) {
            log.warn("serialNo not found certificate,verify failed");
            return false;
        }
        //SHA256withRSA sign
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(certificate);
            signature.update(signStr.getBytes());
            //返回验签结果
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
