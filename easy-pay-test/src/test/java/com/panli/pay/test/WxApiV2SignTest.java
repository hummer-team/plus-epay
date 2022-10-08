package com.panli.pay.test;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.DateUtil;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentReq;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
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
        body.setTimeExpire(DateUtil.addHour(DateUtil.now(), 1));

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
    public void key() {
        System.out.println("0123456789abcdefgopqrstABCDEFGHI".getBytes().length);
    }

    @Test
    public void test() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyLoc0jR0EIhxEAqDUWg0\n" +
                "H21hgguAM1LnGWr6BvsKQ9S7muZ8C+8BCqxn+BaMzgOHENO2CSFa4pIglGk3QAJK\n" +
                "mNlcl7d3KblE4SNpfOKGA3XZWZjVxMlAs8DWrEWE4WJHkwQIAFWw0F14JImUwksa\n" +
                "/E6TJCR8eB29s1odgcPwKqbp1hMPGG7ySfAo7hEkBtcZuqhbUQRTLavzlKVGM8tR\n" +
                "8ZT3arJLtEzlvu3aRP3cBQTrNevQeladS6QG59LiiOr9ykRP/BoYaFhD7dAf/aBc\n" +
                "tCXqnnW8zkc9i1LGZ1VREIczxtTMMcg0zLvORkWzYjNe90ja+TWWFJQHgqEcUnjL\n" +
                "bQIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        privateKey = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
//        System.out.println(s);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(keySpec);
        System.out.println(key);
    }
}
