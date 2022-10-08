package com.panli.pay.service.domain.payment.wx;

import com.panli.pay.integration.wxpayment.WxApiV2Sign;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxBarCodePaymentQueryReq;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH;

@Service(WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH)
@Slf4j
public class WxV2ServiceMerchantPaymentQuery extends WxV2PaymentQuery {
    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public WxBarCodePaymentQueryReq builder(BaseContext<PaymentQueryContext> context) throws Throwable {
        return super.builder(context);
    }

    @Override
    protected Map<String, Object> putRequestBody(BaseContext<PaymentQueryContext> context, ChannelConfigPo configPo)
            throws Exception {
        Map<String, Object> map = super.putRequestBody(context, configPo);
        map.put("sub_mch_id", context.getContext().getSubMchId());
        //before remove the sign
        map.remove("sign");

        map.put("sign", WxApiV2Sign.generateSignatureByMd5(map, configPo.getPrivateKey()));
        return map;
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
    public String doCall(BaseContext<PaymentQueryContext> context, WxBarCodePaymentQueryReq reqContext) throws Throwable {
        return super.doCall(context, reqContext);
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(String resp) throws Throwable {
        return super.parseResp(resp);
    }


    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {
        if (!successOfOption(resultMap)) {
            Object errCode = resultMap.get("err_code");
            if ("ORDERNOTEXIST".equals(errCode)) {
                return PaymentStatusEnum.ORDER_NOT_EXIST;
            }
            return PaymentStatusEnum.UNKNOWN;
        }
        String tradeState = (String) resultMap.get("trade_state");
        switch (tradeState) {
            case "SUCCESS":
                return PaymentStatusEnum.PAYMENT_SUCCESS;
            case "REFUND":
                return PaymentStatusEnum.REFUND_SUCCESS;
            case "NOTPAY":
            case "USERPAYING":
            case "ACCEPT":
            case "PAYERROR":
                return PaymentStatusEnum.WAIT_PAYMENT;
            case "CLOSED":
                return PaymentStatusEnum.PAYMENT_CLOSED;
            case "REVOKED":
                return PaymentStatusEnum.PAYMENT_CANCELED;
            default:
                return PaymentStatusEnum.UNKNOWN;
        }
    }

}
