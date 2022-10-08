package com.panli.pay.service.domain.payment.wx;


import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxAdvancePaymentReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_ADVANCE_PAYMENT_APP_CHANNEL;

/**
 * create weixin payment order, status is wait pay
 *
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml'>预支付统一下单(app)</a>
 */
@Service(WX_ADVANCE_PAYMENT_APP_CHANNEL)
@Slf4j
public class WxV3AdvancePaymentApp extends BaseV3Payment implements PaymentChannel<WxAdvancePaymentReqContext
        , String, PaymentContext, PaymentResultContext> {
    @Autowired
    private WxV3AdvancePayment wxV3AdvancePayment;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxAdvancePaymentReqContext builder(BaseContext<PaymentContext> context)
            throws Throwable {
        return wxV3AdvancePayment.builder(context);
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
    public String doCall(BaseContext<PaymentContext> context,
                         WxAdvancePaymentReqContext reqContext)
            throws Throwable {
        return wxV3AdvancePayment.doCall(context, reqContext);
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<PaymentResultContext> parseResp(String resp) throws Throwable {
        return wxV3AdvancePayment.parseResp(resp);
    }


    @Override
    public boolean successOfOption(Map<String, Object> resultMap) {
        return false;
    }
}
