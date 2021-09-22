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

package com.panli.pay.service.domain.payment.wx;

import com.google.common.base.Strings;
import com.panli.pay.integration.wxpayment.PemUtil;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
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
    /**
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_0.shtml#part-5
     * openssl x509 -in ../../Documents/pubCert/apiclient_cert.pem -noout -serial
     */
    private static final String SERIAL_NO = "118CEEA5924BA344DE49593C625B8C7443E0A776";
    private static final String SIGN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDY6Elg7kp0GZqU\n" +
            "6Gcicqq6C3Gwfs9G0C8/U/Ci7lZe6EavTypgKNOJ3YQE2exX+H8VAZTVUQ2ltmm0\n" +
            "2Yz6lDciO4zMONQLzz+Hf6xQXR+CAcLDojG/m2gavJ6dFRNO+kqBULED6llvhkfg\n" +
            "wdunKB85sYmwPdt9EdyXrkEgwibIYkJzNnMIyQLJeiRWtUsbr8U9YC4IkZ7/aUMd\n" +
            "6i4dJZhJuWNupDKIeFOd7pUCMv8QnDU9R+iGvGWmjU61/3ef1t2E7DV1GS42x2PG\n" +
            "Ut6Ljl+0V5/ceQrKPZpS7kPpYj012y820ISm8uzUtp6Msf96P0no5YboodiNiKwh\n" +
            "5+D96xTlAgMBAAECggEAFeTEW1NSEovvDW6z0kdOsj6reAwksdobmsHHPTrfXu8F\n" +
            "C3brKQ+V4omIZND2SR7c/OrO8gUfYoWZ/9Yho71IH11xZb+qr5J4sB2T7/ymTIIO\n" +
            "/Z4mdVejV2eIfwR8kXE0Cf9yDpod8uLT6HzIxgOHu1QPOMnVmLcv0+OT0VGnhnws\n" +
            "8I6BW1yZQ6s/iVKu7+ZgnVE0GQrsSRwZAKiPec15wVZrHGrhlg1AUyvB8yE+XmLX\n" +
            "lWDugQuqySdfQNGaRB8q/TXl1iGe6ja25fKMqOZrXS5HR9WoL/IbMBsSuMg0TQlI\n" +
            "cGH8Rkjf0+zJ9hGwKJlcVdioQCHRRmqf5gPjSVgttQKBgQDx/P7wb6oHpgnAi9sV\n" +
            "saQSYbSQfWilKtEV2zo9xtQ7UM5D96++G7LrWcx1ve/zCqwIt+DTt6GYpDgxvZn9\n" +
            "rWfYzho3/H9nXxJDgXYiqE2eqZHej33aCIaEUE2Megof7MQPmGCF+iqa/1b/hUPa\n" +
            "L2TLQAMgPsnOmCUyCsxZr/zIIwKBgQDld4PyIu7kN7yU/yCh6AB0rdtQ/FFdrMvM\n" +
            "U520uamT0T6K5uoBczOcL2MOUggWcqNbEY3JJsrGoWv4FPZnf3B8FrkasDvF18K0\n" +
            "lIcvAKltWYCAyJdYA4hK56TTTfLzF+FP7ekji6ZMTqKdfIy/ndhAsW/2K8HFS7tk\n" +
            "FX2LNEE7VwKBgFF1NahgHl9WwB+ltIkX9EAPzcV5wbkfsaRm2LMk9BpHeDee7/TR\n" +
            "xCe/Ybkcx+ILbJ++rhr2zJniZdwwk961+agcsOy5vXVhpnc85DSEbSSEtkqyCM5V\n" +
            "Ylq14XTxO+lFPTZ2t79B2Ae2/mUjShTvMUHN+X9oQ2ydAKBYzDNAr5n7AoGBAM5F\n" +
            "QTbIcDZ2Y+8L6Cmig8ZfAmcqaXnreseR4pIy010tGyuBp5jmFd6wOjsFQn/rTSzv\n" +
            "+qR3WpBzSrsXHOishlEFnB+BjDmsWjo5yctWrPa/HeSmbGWRx1KOKHxJ9brQHJnd\n" +
            "I1XwkuzFQyiObXdSTLLazepcKzNPgIPXTaN0ta7BAoGBANHWO1TV3SQgqF2sGwyx\n" +
            "s0e7mB/Tb50zLXXwsUX3Ry9jGX1I4Kb4UjOp5CGLzcb/1oKePax+2M+fawimIJpv\n" +
            "wrMfV78id2RNE4cQdhwPnDQtxSAsLm/Sf9hRxKK63K7rEcFrw+aeuLN4EwRp9vnf\n" +
            "9+oTkxR43juaAbdrWZT/muAT\n" +
            "-----END PRIVATE KEY-----";

    @Override
    public Header signOfHeader(String mchid, String httpMethod, String url, String body) throws Exception {
        String sign = getRequestToken(mchid, httpMethod, url, body);
        return new BasicHeader("Authorization", sign);
    }

    public String getRequestToken(String mchid, String httpMethod, String url, String body) throws Exception {
        return String.format("%s %s", WX_API_V3_SIGN_SCHEMA, sign(mchid, httpMethod, new URL(url), body));
    }

    private String sign(String mchid, String httpMethod, URL url, String body) throws Exception {
        String nonceStr = WxApiV2Sign.generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = formatMessage(httpMethod, url, timestamp, nonceStr, body);
        String signature = sign(message);

        String signStr = "mchid=\"" + mchid + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + SERIAL_NO + "\","
                + "signature=\"" + signature + "\"";

        log.info("wx api request sign is :{}", signStr);

        return signStr;
    }

    private String sign(String message)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(PemUtil.loadPrivateKey(
                new ByteArrayInputStream(SIGN_PRIVATE_KEY.getBytes(StandardCharsets.UTF_8))));
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
