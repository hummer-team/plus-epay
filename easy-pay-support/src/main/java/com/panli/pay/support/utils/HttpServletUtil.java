package com.panli.pay.support.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

public class HttpServletUtil {
    private HttpServletUtil() {

    }

    public static Map<String, Enumeration<String>> getAllHeadOfMap(HttpServletRequest request) {
        Enumeration<String> headNames = request.getHeaderNames();
        Map<String, Enumeration<String>> headMap = new java.util.concurrent.ConcurrentHashMap<>();
        while (headNames.hasMoreElements()) {
            String name = headNames.nextElement();
            headMap.put(name, request.getHeaders(name));
        }
        return headMap;
    }

    public static String getAllHeadOfString(HttpServletRequest request) {
        Map<String, Enumeration<String>> map = getAllHeadOfMap(request);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Enumeration<String>> entry : map.entrySet()) {
            stringBuilder.append(String.format("%s=%s,", entry.getKey(), entry.getValue().toString()));
        }

        return stringBuilder.toString();
    }
}
