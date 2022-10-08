package com.panli.pay.service.domain.payment.ali;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hummer.common.utils.BigDecimalUtil;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_REFUND_CHANNEL;

@Service(ALI_REFUND_CHANNEL)
@Slf4j
public class AliRefund extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradeRefundRequest>, AlipayTradeRefundResponse, RefundContext, RefundResultContext> {
    /**
     * builder request body
     *
     * @param context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradeRefundRequest> builder(BaseContext<RefundContext> context) {
        AliPaymentChannelReqContext<AlipayTradeRefundRequest> reqContext = new AliPaymentChannelReqContext<>();
        reqContext.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        String body = String.format("{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"trade_no\":\"%s\"," +
                        "\"out_request_no\":\"%s\"," +
                        "\"refund_amount\":\"%s\"}"
                , context.getContext().getTradeId()
                , context.getContext().getChannelTradeId()
                , context.getContext().getRefundBatchId()
                , context.getContext().getAmount());
        request.setBizContent(body);
        reqContext.setAliPayRequest(request);
        reqContext.setRequestBody(body);
        //reqContext.setData(reqContext);
        return reqContext;
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
    public AlipayTradeRefundResponse doCall(BaseContext<RefundContext> context,
                                            AliPaymentChannelReqContext<AlipayTradeRefundRequest> reqContext)
            throws Throwable {
        return reqContext.getAlipayClient().execute(reqContext.getAliPayRequest());
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(AlipayTradeRefundResponse resp) {
        RefundResultContext context = RefundResultContext.<RefundResultContext>builder()
                .amount(BigDecimalUtil.valueOf(resp.getRefundFee()))
                .channelOriginResponse(JSON.toJSONString(resp))
                .channelRespCode(resp.getCode())
                .channelSubCode(resp.getSubCode())
                .channelRespMessage(resp.getSubMsg())
                .success(resp.isSuccess())
                .build();

        context.setResult(context);
        return context;
    }
}
