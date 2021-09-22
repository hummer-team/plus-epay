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

package com.panli.pay.integration.wxpayment;

import com.hummer.common.exceptions.AppException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PemUtil {

  public static PrivateKey loadPrivateKey(InputStream inputStream) {
    try {
      ByteArrayOutputStream array = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        array.write(buffer, 0, length);
      }

      String privateKey = array.toString("utf-8")
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");

      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePrivate(
              new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    } catch (Throwable e) {
      throw new AppException(50000, "wx api v3 sign failed", e);
    }
  }

  public static X509Certificate loadCertificate(InputStream inputStream) {
    try {
      CertificateFactory cf = CertificateFactory.getInstance("X509");
      X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
      cert.checkValidity();
      return cert;
    } catch (Throwable e) {
      throw new AppException(50000, "wx api v3 Certificate failed", e);
    }
  }
}
