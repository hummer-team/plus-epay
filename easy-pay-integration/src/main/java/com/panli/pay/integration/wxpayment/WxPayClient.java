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

import com.google.common.base.Strings;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.http.HttpResult;
import com.hummer.common.http.HttpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.util.concurrent.TimeUnit;

import static com.panli.pay.integration.wxpayment.WXPayConstant.USER_AGENT;

@Slf4j
public class WxPayClient {

    public static String doPost(String uri, String xmlBody, String machId, long timeoutMillis, int retry) {
        return doPost(uri, xmlBody, machId, timeoutMillis, null, retry);
    }

    public static String doPost(String uri, String xmlBody, String machId, long timeoutMillis
            , String certName, int retry) {

        HttpResult result = doPostWith(uri, xmlBody, ContentType.TEXT_XML.withCharset("UTF-8").toString()
                , timeoutMillis, certName, retry
                , new BasicHeader("User-Agent", USER_AGENT + " " + machId));
        log.debug("call wx payment service result is, req:{} - resp:{}", xmlBody, result);
        if (result.getStatus() != 200) {
            throw new AppException(result.getStatus(), String.format("call wx payment service failed, response status %s"
                    , result.getStatus()));
        }
        if (Strings.isNullOrEmpty(result.getResult())) {
            throw new AppException(result.getStatus(), "call wx payment service failed,service no response content");
        }
        return result.getResult();
    }

    public static HttpResult doPostWith(String uri, String body, String contentType, long timeoutMillis
            , int retry, Header... header) {
        return doPostWith(uri, body, contentType, timeoutMillis, null, retry, header);
    }

    public static HttpResult doGet(String uri, String contentType, long timeoutMillis
            , int retry, Header... header) {
        return doGet(uri, contentType, timeoutMillis, null, retry, header);
    }

    public static HttpResult doGet(String uri, long timeoutMillis
            , int retry, Header... header) {
        return doGet(uri, null, timeoutMillis, null, retry, header);
    }

    public static HttpResult doGet(String uri, String contentType, long timeoutMillis
            , String certName, int retry, Header... header) {
        HttpGet requestBase = new HttpGet(uri);
        for (Header httpHead : header) {
            requestBase.addHeader(httpHead);
        }
        return HttpSyncClient.execute2ResultByRetry(certName
                , requestBase
                , timeoutMillis
                , TimeUnit.MILLISECONDS
                , retry
                , false);
    }

    public static HttpResult doPostWith(String uri, String body, String contentType, long timeoutMillis
            , String certName, int retry, Header... header) {
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        stringEntity.setContentType(contentType);
        HttpPost requestBase = new HttpPost(uri);
        for (Header httpHead : header) {
            requestBase.addHeader(httpHead);
        }
        requestBase.setEntity(stringEntity);

        return HttpSyncClient.execute2ResultByRetry(certName
                , requestBase
                , timeoutMillis
                , TimeUnit.MILLISECONDS
                , retry
                , false);
    }



}
