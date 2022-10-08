package com.panli.pay.service.domain.payment.wx;

import com.hummer.common.utils.AppBusinessAssert;
import com.hummer.common.utils.DateUtil;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodePaymentReqContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1'>服务商条码支付-创建分账订单</a>
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
        AppBusinessAssert.isTrue(context.getContext().getPaymentTimeOut() != null
                , 400, "扫码支付支付截止时间必传");

        Map<String, Object> map = super.putRequestBody(context, barCode, configPo);
        String subAppId = String.valueOf(context.getContext().getAffixData().get("subAppId"));
        if (StringUtils.isNotEmpty(subAppId) && !"null".equals(subAppId)) {
            map.put("sub_appid", subAppId);
        }
        map.put("sub_mch_id", context.getContext().getAffixData().get("subMchId"));
        map.put("profit_sharing", convertProfitSharingValue(context, "profitSharing"));
        map.put("time_expire", DateUtil.dateFormat(context.getContext().getPaymentTimeOut()
                , DateUtil.DateTimeFormat.F4));
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

    @Override
    public boolean successOfOption(Map<String, Object> resultMap) {
        Object tradeType = resultMap.get("trade_type");
        return super.successOfOption(resultMap) && "MICROPAY".equals(tradeType);
    }

    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {

        if (successOfOption(resultMap)) {
            return PaymentStatusEnum.PAYMENT_SUCCESS;
        }
        String errCode = (String) resultMap.get("err_code");
        if (errCode == null) {
            return PaymentStatusEnum.PAYMENT_FAILED;
        }
        switch (errCode) {
            case "SYSTEMERROR":
            case "BANKERROR":
            case "USERPAYING":
                return PaymentStatusEnum.WAIT_PAYMENT;
            default:
                return PaymentStatusEnum.PAYMENT_FAILED;
        }
    }
}
