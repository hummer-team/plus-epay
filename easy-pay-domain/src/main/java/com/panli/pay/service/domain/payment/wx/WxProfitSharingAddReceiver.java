package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.integration.wxpayment.PemUtil;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.ProfitSharingAddReceiverContext;
import com.panli.pay.service.domain.result.ProfitSharingAddReceiverResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxProfitSharingAddReceiverContext;
import com.panli.pay.support.model.bo.payment.WxServiceMerchantAddReceiverRespDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_ADD_RECEIVER;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml'>请求分账</a>
 */
@Service(WX_FZ_ADD_RECEIVER)
@Slf4j
public class WxProfitSharingAddReceiver extends BaseV3Payment implements PaymentChannel<WxProfitSharingAddReceiverContext
        , String, ProfitSharingAddReceiverContext, ProfitSharingAddReceiverResultContext> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxProfitSharingAddReceiverContext builder(BaseContext<ProfitSharingAddReceiverContext> context) throws Throwable {

        WxProfitSharingAddReceiverContext req = ObjectCopyUtils.copy(context, WxProfitSharingAddReceiverContext.class);
        req.setAppId(context.getChannelConfigPo().getAppId());
        req.setServiceUrl(context.getChannelConfigPo().getServiceUrl());
        req.setTimeoutMillis(context.getChannelConfigPo().getConnectTimeoutMs());
        req.setMerchantId(context.getChannelConfigPo().getMerchantId());
        req.setName(PemUtil.rsaEncryptOAEP(req.getName()
                , PemUtil.loadCertificate(context.getChannelConfigPo().getChannelCertPo().getCertPlatformPri())));
        return req;
    }

    /**
     * call channel service
     *
     * @param context    context
     * @param reqContext reqContext
     * @return
     * @throws Throwable
     */
    @Override
    public String doCall(BaseContext<ProfitSharingAddReceiverContext> context, WxProfitSharingAddReceiverContext reqContext) throws Throwable {
        String body = JSON.toJSONString(reqContext);
        String s = WxPayClient.doV3Post(reqContext.getServiceUrl()
                , body
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getMerchantId(), "POST"
                        , reqContext.getServiceUrl(), body, context.getChannelConfigPo().getChannelCertPo())
                , new BasicHeader("Wechatpay-Serial", context.getChannelConfigPo().getChannelCertPo().getCertPlatformSerialNo())
        );

        return s;
    }

    /**
     * parse channel response to local context
     *
     * @param resp resp channel response raw content
     * @return {@link BaseResultContext}
     */
    @Override
    public BaseResultContext<ProfitSharingAddReceiverResultContext> parseResp(String resp) throws Throwable {
        WxServiceMerchantAddReceiverRespDto respDto = JSON.parseObject(resp
                , new TypeReference<WxServiceMerchantAddReceiverRespDto>() {
                });
        ProfitSharingAddReceiverResultContext context = ObjectCopyUtils.copy(respDto
                , ProfitSharingAddReceiverResultContext.class);
        context.setSuccess(successOfOption(null));
        context.setChannelCode("200");
        context.setChannelRespMessage(null);
        context.setChannelOriginRespDto(respDto);
        context.setChannelOriginResponse(resp);
        return context;
    }

    @Override
    public PaymentStatusEnum parsePaymentStatus(Map<String, Object> resultMap) {

        return null;
    }

    @Override
    public boolean successOfOption(Map<String, Object> resultMap) {
        return true;
    }

}
