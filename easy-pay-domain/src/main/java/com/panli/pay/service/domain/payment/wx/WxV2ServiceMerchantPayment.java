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

package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.PaymentResultContext;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodePaymentReqContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1'>?????????????????????-??????????????????</a>
 */
@Service(WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH)
@Slf4j
public class WxV2ServiceMerchantPayment extends WxV2Payment {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxBarCodePaymentReqContext builder(BaseContext<PaymentContext> context) throws Throwable {
        return super.builder(context);
    }

    @Override
    protected Map<String, Object> putRequestBody(BaseContext<PaymentContext> context
            , String barCode
            , ChannelConfigPo configPo) throws Exception {
        Map<String, Object> map = super.putRequestBody(context, barCode, configPo);
        String subAppId = String.valueOf(context.getContext().getAffixData().get("subAppId"));
        if (StringUtils.isNotEmpty(subAppId) && !"null".equals(subAppId)) {
            map.put("sub_appid", subAppId);
        }
        map.put("sub_mch_id", context.getContext().getAffixData().get("subMchId"));
        map.put("profit_sharing", convertProfitSharingValue(context, "profitSharing"));
        //before remove the sign
        map.remove("sign");
        map.put("sign", WxApiV2Sign.generateSignatureByMd5(map, configPo.getPrivateKey()));
        return map;
    }

    private String convertProfitSharingValue(BaseContext<PaymentContext> context, String profitSharing) {
        Object o = context.getContext().getAffixData().get(profitSharing);
        log.info("order {} enable profit sharing value is {} ", context.getContext().getTradeId(), o);
        if (o == null) {
            return "N";
        }

        if (Boolean.TRUE.equals(Boolean.valueOf(o.toString()))) {
            return "Y";
        }
        return "N";
    }

    /**
     * call channel service
     *
     * @param context
     * @param reqContext
     * @return
     * @throws Throwable
     */
    @Override
    public String doCall(BaseContext<PaymentContext> context, WxBarCodePaymentReqContext reqContext) throws Throwable {
        return super.doCall(context, reqContext);
    }

    /**
     * parse channel response to local context
     *
     * @param resp channel response raw content
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(String resp) throws Throwable {
        return super.parseResp(resp);
    }

    @Override
    public boolean successOfBarCode(Map<String, Object> resultMap) {
        return super.successOfBarCode(resultMap);
    }

    /**
     * builder response message
     *
     * @param result      {@link BaseResultContext}
     * @param channelResp channel response message
     * @return response body is map
     */
    @Override
    public BasePaymentResp<? extends BasePaymentResp<?>> builderRespMessage(
            BaseResultContext<PaymentResultContext> result, String channelResp) {
        return super.builderRespMessage(result, channelResp);
    }
}
