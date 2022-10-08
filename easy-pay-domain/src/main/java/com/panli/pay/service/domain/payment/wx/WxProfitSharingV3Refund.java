package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.AppBusinessAssert;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.ProfitSharingReturnContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.payment.wx.context.req.WxProfitSharingReturnReqDto;
import com.panli.pay.service.domain.payment.wx.context.resp.WxProfitSharingRefundRespDto;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_RETURN;

@Service(WX_FZ_RETURN)
public class WxProfitSharingV3Refund extends BaseV3Payment implements PaymentChannel<WxProfitSharingReturnReqDto
        , String, ProfitSharingReturnContext, RefundResultContext> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxProfitSharingReturnReqDto builder(BaseContext<ProfitSharingReturnContext> context)
            throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        WxProfitSharingReturnReqDto dto = ObjectCopyUtils.copy(context, WxProfitSharingReturnReqDto.class);
        dto.setServiceUrl(configPo.getServiceUrl());
        dto.setAmount(convertAmount(((ProfitSharingReturnContext) context).getAmount()));
        dto.setTimeoutMillis(configPo.getConnectTimeoutMs());
        return dto;
    }

    /**
     * call channel service
     */
    @Override
    public String doCall(BaseContext<ProfitSharingReturnContext> context
            , WxProfitSharingReturnReqDto reqDto) throws Throwable {
        ChannelConfigPo configPo = context.getChannelConfigPo();
        String body = reqDto.requestBody();
        return WxPayClient.doV3Post(reqDto.getServiceUrl()
                , body
                , reqDto.getTimeoutMillis()
                , reqDto.getRetry()
                , apiV3Sign.signOfHeader(configPo.getMerchantId(), "POST"
                        , reqDto.getServiceUrl(), body, configPo.getChannelCertPo())
        );
    }

    /**
     * parse channel response to local context
     *
     * @param resp
     * @return
     */
    @Override
    public BaseResultContext<RefundResultContext> parseResp(String resp) throws Throwable {
        WxProfitSharingRefundRespDto respDto = JSON.parseObject(resp, WxProfitSharingRefundRespDto.class);
        AppBusinessAssert.isTrue(!"FAILED".equalsIgnoreCase(respDto.getResult())
                , 400, respDto.getFailReason());
        RefundResultContext result = RefundResultContext.builder()
                .channelOriginResponse(resp)
                .channelRespMessage(respDto.getFailReason())
                .success(true)
                .channelRespCode(respDto.getResult())
                .channelRefundId(respDto.getReturnId())
                .build();
        result.setResult(result);
        return result;
    }
}
