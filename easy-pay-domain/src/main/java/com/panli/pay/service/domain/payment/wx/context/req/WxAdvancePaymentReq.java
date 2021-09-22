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

package com.panli.pay.service.domain.payment.wx.context.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WxAdvancePaymentReq {
    private String appid;
    private String mchid;
    private String description;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "time_expire", format = "yyyy-MM-ddTHH:mm:ss.SSSZ")
    private Date timeExpire;
    @JSONField(name = "notify_url")
    private String notifyUrl;
    private String attach;
    @JSONField(name = "goods_tag")
    private String goodsTag;
    private Amount amount;
    private Payer payer;
    private SceneInfo sceneInfo;
    private Detail detail;

    @Data
    public static class Amount {
        private int total;
        private String currency;
    }

    @Data
    public static class Payer {
        private String openid;
    }

    @Data
    public static class SceneInfo {
        @JSONField(name = "payer_client_ip")
        private String payerClientIp;
        @JSONField(name = "device_id")
        private String deviceId;
        @JSONField(name = "store_info")
        private StoreInfo storeInfo;
    }

    @Data
    public static class StoreInfo {
        public String id;
        private String name;
    }

    @Data
    public static class Detail {
        @JSONField(name = "cost_price")
        private Integer costPrice;
        @JSONField(name = "goods_detail")
        private List<GoodsDetail> goodsDetail;
    }

    @Data
    public static class GoodsDetail {
        @JSONField(name = "merchant_goods_id")
        private String merchantGoodsId;
        @JSONField(name = "goods_name")
        private String goodsName;
        private Integer quantity;
        @JSONField(name = "unit_price")
        private Integer unitPrice;
    }
}
