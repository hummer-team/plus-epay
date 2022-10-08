package com.panli.pay.service.domain.payment.ali;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.result.PaymentQueryResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.ali.context.AliPaymentChannelReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.ALI_PAY_REFUND_QUERY_CHANNEL;

/**
 * @author lee
 * @see <a href='https://opendocs.alipay.com/apis/api_1/alipay.trade.fastpay.refund.query'>统一退款查询</a>
 */
@Service(ALI_PAY_REFUND_QUERY_CHANNEL)
@Slf4j
public class AliRefundQuery extends BaseAliPayment implements
        PaymentChannel<AliPaymentChannelReqContext<AlipayTradeFastpayRefundQueryRequest>, AlipayTradeFastpayRefundQueryResponse
                , PaymentContext, PaymentQueryResultContext> {
    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public AliPaymentChannelReqContext<AlipayTradeFastpayRefundQueryRequest> builder(BaseContext<PaymentContext> context)
            throws Throwable {
        AliPaymentChannelReqContext<AlipayTradeFastpayRefundQueryRequest> refundQuery = new AliPaymentChannelReqContext<>();
        refundQuery.setAlipayClient(builderAlipayClient(context.getChannelConfigPo()));

        String refundQueryPar = String.format("{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"trade_no\":\"%s\"}", context.getContext().getTradeId()
                , context.getContext().getChannelTradeId());

        AlipayTradeFastpayRefundQueryRequest req = new AlipayTradeFastpayRefundQueryRequest();
        req.setBizContent(refundQueryPar);

        refundQuery.setAliPayRequest(req);
        refundQuery.setRequestBody(refundQueryPar);
        return refundQuery;
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
    public AlipayTradeFastpayRefundQueryResponse doCall(BaseContext<PaymentContext> context, AliPaymentChannelReqContext<AlipayTradeFastpayRefundQueryRequest> reqContext) throws Throwable {
        return reqContext.getAlipayClient().execute(reqContext.getAliPayRequest());
    }

    /**
     * parse channel response to local context
     *
     * @param resp channel response body
     * @return {@link BaseResultContext}
     */
    @Override
    public BaseResultContext<PaymentQueryResultContext> parseResp(AlipayTradeFastpayRefundQueryResponse resp) throws Throwable {
        return PaymentQueryResultContext.builder()
                .success(resp.isSuccess())
                .tradeId(resp.getOutTradeNo())
                .channelTradeId(resp.getTradeNo())
                .status(parsePaymentStatus(null))
                .describe(resp.getRefundStatus())
                .channelOriginResponse(JSON.toJSONString(resp))
                .build();
    }

    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {

        // TODO status
        return super.parsePaymentStatus(resultMap);
    }
}
