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

package com.panli.pay.test;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.DateUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lee
 * @since:1.0.0
 * @Date: 2019/7/9 14:42
 **/
public class SimpleDemoFacadeTest extends BaseTest {
    @Test
    public void json() {
        Map<String, Object> map = new HashMap<>(3);
        map.put("A", null);
        map.put("B", "D");
        map.put("C", "s");

        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void dateString() {
        System.out.println(String.format("YG%s", DateUtil.formatNowDate("yyyyMMddHHmmssSSS")));
    }
}
