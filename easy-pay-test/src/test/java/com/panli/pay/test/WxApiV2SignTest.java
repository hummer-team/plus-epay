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
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentReq;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WxApiV2SignTest {

    @Test
    public void asXml() throws DocumentException {
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        sortedMap.put("appid", 12344);
        sortedMap.put("auth_code", "ssss");
        sortedMap.put("spbill_create_ip", "ssssse");
        sortedMap.put("sign", "C29DB7DB1FD4136B84AE35604756362C");

        String xml = WxApiV2Sign.mapToXml(sortedMap);

        System.out.println(xml);

        Map<String, Object> map = WxApiV2Sign.xmlToMap(xml);
        System.out.println(map);
    }

    @Test
    public void wxAdvancePaymentReqBody() {
        WxAdvancePaymentReq body = new WxAdvancePaymentReq();
        body.setAppid("wx0000000001");
        body.setMchid("wx00000000001");
        body.setDescription("test");
        body.setOutTradeNo("id000000001");
        body.setNotifyUrl("url000000001");
        body.setTimeExpire(DateUtil.addHour(DateUtil.now(),1));

        WxAdvancePaymentReq.Amount amount = new WxAdvancePaymentReq.Amount();
        amount.setTotal(1);
        amount.setCurrency("SSS");
        body.setAmount(amount);

        WxAdvancePaymentReq.Payer payer = new WxAdvancePaymentReq.Payer();
        payer.setOpenid("openId00000001");
        body.setPayer(payer);


        WxAdvancePaymentReq.Detail detail = new WxAdvancePaymentReq.Detail();

        WxAdvancePaymentReq.GoodsDetail goodsDetail = new WxAdvancePaymentReq.GoodsDetail();
        goodsDetail.setGoodsName("goodsName");
        goodsDetail.setQuantity(1);
        goodsDetail.setUnitPrice(1);
        detail.setGoodsDetail(Collections.singletonList(goodsDetail));

        body.setDetail(detail);


        System.out.println(JSON.toJSONString(body));
    }


    @Test
    public void key(){
        System.out.println("0123456789abcdefgopqrstABCDEFGHI".getBytes().length);
    }
}
