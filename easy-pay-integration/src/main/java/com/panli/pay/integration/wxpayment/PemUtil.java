package com.panli.pay.integration.wxpayment;

import com.alibaba.fastjson.JSON;
import com.hummer.common.exceptions.AppException;
import com.panli.pay.integration.wxpayment.response.CertificatResponseDto;
import com.panli.pay.support.utils.AesUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PemUtil {

    public static PrivateKey loadPrivateKey(String signKey) {
        try {
            String privateKey = signKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (Throwable e) {
            throw new AppException(50000, "wx api v3 sign failed", e);
        }
    }

    public static Map<String, X509Certificate> parseCertContent(String content, String key) throws GeneralSecurityException, IOException {
        Map<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();

        List<CertificatResponseDto> certificateList = JSON.parseArray(
                JSON.parseObject(content).getString("data"), CertificatResponseDto.class);
        //最新时间
        for (CertificatResponseDto cert : certificateList) {
            CertificatResponseDto.EncryptCertificate encryptCertificate = cert.getEncryptCertificate();
            //获取证书字公钥
            String publicKey = new AesUtil(key.getBytes(StandardCharsets.UTF_8)).decryptToString(
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


    public static X509Certificate loadCertificate(String publicKey) throws CertificateException {
        //获取证书字公钥
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        //获取证书
        ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKey.getBytes(StandardCharsets.UTF_8));
        return (X509Certificate) cf.generateCertificate(inputStream);
    }

    public static String rsaEncryptOAEP(String message, X509Certificate certificate)
            throws IllegalBlockSizeException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            byte[] cipherdata = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(cipherdata);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的证书", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
        }
    }
}
