package com.panli.pay.service.domain.services;

import javax.servlet.http.HttpServletRequest;

public interface WxResponseService {
    boolean verify(HttpServletRequest request,String jsonBody);
    String tryDecryptBody(String body, String nonce, String ciphertext);
}
