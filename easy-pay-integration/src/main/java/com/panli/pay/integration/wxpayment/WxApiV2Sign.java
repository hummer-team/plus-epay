package com.panli.pay.integration.wxpayment;

import com.google.common.base.Strings;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.panli.pay.integration.wxpayment.WXPayConstant.SYMBOLS;

public final class WxApiV2Sign {

    private static final Random RANDOM = new SecureRandom();

    private WxApiV2Sign() {

    }

    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static String generateSignature(final Map<String, Object> data, String key
            , WXPayConstant.SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstant.FIELD_SIGN)) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k) != null && data.get(k).toString().trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).toString().trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        if (WXPayConstant.SignType.MD5.equals(signType)) {
            return mD5(sb.toString()).toUpperCase();
        } else if (WXPayConstant.SignType.HMACSHA256.equals(signType)) {
            return hMACSHA256(sb.toString(), key);
        } else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }


    public static String mD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static String hMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static String mapToXml(Map<String, Object> data) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("xml");

        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.toString().trim();
            root.addElement(key).addText(value.toString());
        }
        return document.getRootElement().asXML();
    }

    public static Map<String, Object> xmlToMap(String xml) throws DocumentException {
        if (Strings.isNullOrEmpty(xml)) {
            return Collections.emptyMap();
        }
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Iterator<Element> elementIterator = root.elementIterator();
        Map<String, Object> map = new ConcurrentHashMap<>(16);
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            map.put(element.getName(), element.getData());
        }
        return map;
    }

    public static String generateSignatureByMd5(final Map<String, Object> data, final String key) throws Exception {
        return generateSignature(data, key, WXPayConstant.SignType.MD5);
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }
}
