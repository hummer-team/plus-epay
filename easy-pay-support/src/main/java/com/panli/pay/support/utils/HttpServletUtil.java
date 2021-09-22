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
