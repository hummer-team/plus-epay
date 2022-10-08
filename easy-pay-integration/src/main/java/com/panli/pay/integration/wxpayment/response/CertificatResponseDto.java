package com.panli.pay.integration.wxpayment.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class CertificatResponseDto {
    /**
     * 平台证书序列号
     */
    @JSONField(name = "serial_no")
    private String serialNo;
    @JSONField(name = "effective_time")
    private Date effectiveTime;
    @JSONField(name = "expire_time")
    private Date expireTime;

    /**
     * 加密证书
     */
    @JSONField(name = "encrypt_certificate")
    private transient EncryptCertificate encryptCertificate;

    private String cert;

    @Data
    public static class EncryptCertificate {
        private String algorithm;
        private String nonce;

        /**
         * 相关数据
         */
        @JSONField(name = "associated_data")
        private String associatedData;

        /**
         * 密文
         */
        private String ciphertext;
    }
}
