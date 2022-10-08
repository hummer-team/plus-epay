package com.panli.pay.service.domain.payment.wx;

import com.google.common.base.Strings;
import com.hummer.common.SysConstant;
import com.hummer.common.utils.AppBusinessAssert;
import com.panli.pay.integration.wxpayment.PemUtil;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.support.model.po.ChannelCertPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

@Slf4j
@Component
public class ApiV3Sign implements ApiSign {
    private static final String WX_API_V3_SIGN_SCHEMA = "WECHATPAY2-SHA256-RSA2048";

    @Override
    public Header signOfHeader(String mchid, String httpMethod, String url, String body
            , ChannelCertPo cert) throws Exception {
        AppBusinessAssert.isTrue(cert != null, SysConstant.SYS_ERROR_CODE
                , "channel wx payment cert not exists");

        String authorization = createAuthorization(mchid, httpMethod, url, body, cert.getSerialNo()
                , cert.getApplicationKey());
        return new BasicHeader("Authorization", authorization);
    }

    @Override
    public Header signOfHeader(ChannelCertPo cert) throws Exception {
        String authorization = createAuthorization(cert.getMerchantId()
                , cert.getTokenApiMethod(), cert.getTokenApiUrl(), null, cert.getSerialNo()
                , cert.getApplicationKey());
        return new BasicHeader("Authorization", authorization);
    }

    public String createAuthorization(String mchId, String httpMethod, String url, String body
            , String certSerialNo, String certPrivateKey) throws Exception {
        if (body == null) {
            body = "";
        }
        return String.format("%s %s", WX_API_V3_SIGN_SCHEMA, sign(mchId, httpMethod, new URL(url), body
                , certSerialNo, certPrivateKey));
    }

    private String sign(String mchId, String httpMethod, URL url, String body, String certSerialNo
            , String certPrivateKey) throws Exception {
        String nonceStr = WxApiV2Sign.generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = formatMessage(httpMethod, url, timestamp, nonceStr, body);
        String signature = sign(message, certPrivateKey);

        String signStr = "mchid=\"" + mchId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + certSerialNo + "\","
                + "signature=\"" + signature + "\"";

        log.info("wx api request sign is :{}", signStr);

        return signStr;
    }

    private String sign(String message, String signKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(PemUtil.loadPrivateKey(signKey));
        sign.update(message.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    private String formatMessage(String method, URL url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.getPath();
        if (url.getQuery() != null) {
            canonicalUrl += "?" + url.getQuery();
        }
        String bodyTemp = Strings.isNullOrEmpty(body) ? "" : body;
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + bodyTemp + "\n";
    }
}
