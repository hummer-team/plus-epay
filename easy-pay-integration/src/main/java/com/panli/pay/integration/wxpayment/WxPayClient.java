package com.panli.pay.integration.wxpayment;

import com.google.common.base.Strings;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.http.HttpResult;
import com.hummer.common.http.HttpSyncClient;
import com.hummer.common.http.bo.HttpCertBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
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

    public static String doV2Post(String uri, String xmlBody, String machId, int timeoutMillis, int retry) {
        return doV2Post(uri, xmlBody, machId, timeoutMillis, retry, null);
    }

    public static String doV2Post(String uri, String xmlBody, String machId, int timeoutMillis, int retry
            , HttpCertBo cert) {
        Header[] headers = new Header[]{new BasicHeader("Content-Type", ContentType.TEXT_XML.withCharset("UTF-8").toString())
                , new BasicHeader("User-Agent", USER_AGENT + " " + machId)};
        String s = doPostWith(uri, xmlBody, timeoutMillis, retry, cert, headers);

        log.debug("call wx payment service result is, req:{} - resp:{}", xmlBody, s);
        return s;
    }

    private static String parseHttpResult(HttpResult result) {
        if (result.getStatus() != 200) {
            throw new AppException(result.getStatus(), String.format("call wx payment service failed, response status %s"
                    , result.getStatus()));
        }
        if (Strings.isNullOrEmpty(result.getResult())) {
            throw new AppException(result.getStatus(), "call wx payment service failed,service no response content");
        }
        return result.getResult();
    }


    public static String doGet(String uri, String contentType, long timeoutMillis
            , int retry, Header... header) {
        return doGet(uri, contentType, timeoutMillis, null, retry, header);
    }

    public static String doGet(String uri, long timeoutMillis
            , int retry, Header... header) {
        return doGet(uri, null, timeoutMillis, null, retry, header);
    }

    public static String doGet(String uri, String contentType, long timeoutMillis
            , String certName, int retry, Header... header) {
        HttpGet requestBase = new HttpGet(uri);
        for (Header httpHead : header) {
            requestBase.addHeader(httpHead);
        }
        HttpResult s = HttpSyncClient.execute2ResultByRetry(certName
                , requestBase
                , timeoutMillis
                , TimeUnit.MILLISECONDS
                , retry
                , false);
        return parseHttpResult(s);
    }

    public static String doV3Post(String uri, String body, long timeoutMillis, int retry, Header... headers) {
        Header[] addHeaders = new Header[]{new BasicHeader("Content-Type", "application/json")
                , new BasicHeader("Accept", "application/json")};
        if (ArrayUtils.isEmpty(headers)) {
            headers = addHeaders;
        } else {
            for (Header header : addHeaders) {
                headers = ArrayUtils.add(headers, header);
            }
        }
        return doPostWith(uri, body, timeoutMillis, retry, null, headers);
    }


    private static String doPostWith(String uri, String body, long timeoutMillis
            , int retry, HttpCertBo cert, Header... header) {
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        HttpPost requestBase = new HttpPost(uri);
        for (Header httpHead : header) {
            requestBase.addHeader(httpHead);
        }
        requestBase.setEntity(stringEntity);

        HttpResult result = HttpSyncClient.execute2ResultByRetry(requestBase
                , timeoutMillis
                , TimeUnit.MILLISECONDS
                , retry
                , false, cert);
        return parseHttpResult(result);
    }

}
