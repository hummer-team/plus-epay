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

package com.panli.pay.support.model.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hummer.core.exceptions.KeyNotExistsException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ChannelConfigPo extends BasePo {
    private String channelCode;
    private String merchantId;
    private String channelName;
    private String publicKey;
    private String privateKey;
    private String extendParameter;
    private String serviceUrl;
    private Integer connectTimeoutMs;
    private Integer redTimeoutMs;
    private String appId;

    public String getExtParameterVal(String key) {
        return getExtParameterVal(key, null);
    }

    public String getExtParameterVal(String key, String defaultVal) {
        if (StringUtils.isEmpty(extendParameter)) {
            return defaultVal;
        }

        JSONObject obj = JSON.parseObject(defaultVal);
        String val = obj.getString(key);
        return StringUtils.isNotEmpty(val) ? val : defaultVal;
    }

    public String getExtParameterValWithAssertNotNull(String key) {
        String val = getExtParameterVal(key, null);
        if (StringUtils.isEmpty(val)) {
            throw new KeyNotExistsException(40004, key + " not fund.");
        }
        return val;
    }
}
