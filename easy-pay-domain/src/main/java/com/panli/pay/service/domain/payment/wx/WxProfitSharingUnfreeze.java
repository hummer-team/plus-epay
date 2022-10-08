package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.ProfitSharingUnfreezeContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxProfitSharingUnfreezeReqDto;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.DefaultResult;
import com.panli.pay.support.model.bo.payment.WxServiceMerchantAddReceiverRespDto;
import com.panli.pay.support.model.po.ChannelConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_UNFREEZE;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml'>请求分账</a>
 */
@Service(WX_FZ_UNFREEZE)
@Slf4j
public class WxProfitSharingUnfreeze extends BaseV3Payment implements PaymentChannel<WxProfitSharingUnfreezeReqDto
        , String, ProfitSharingUnfreezeContext, DefaultResult> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxProfitSharingUnfreezeReqDto builder(BaseContext<ProfitSharingUnfreezeContext> context) throws Throwable {
        ProfitSharingUnfreezeContext context1 = (ProfitSharingUnfreezeContext) context;
        WxProfitSharingUnfreezeReqDto req = ObjectCopyUtils.copy(context1, WxProfitSharingUnfreezeReqDto.class);
        req.setOutOrderNo(context1.getProfitSharingCode());
        req.setServiceUrl(context.getChannelConfigPo().getServiceUrl());
        req.setTimeoutMillis(context.getChannelConfigPo().getConnectTimeoutMs());
        req.setTransactionId(context1.getChannelTradeId());
        return req;
    }

    /**
     * call channel service
     *
     * @param context context
     * @return
     * @throws Throwable
     */
    @Override
    public String doCall(BaseContext<ProfitSharingUnfreezeContext> context, WxProfitSharingUnfreezeReqDto reqDto) throws Throwable {
        ChannelConfigPo config = context.getChannelConfigPo();
        String body = JSON.toJSONString(reqDto);
        String s = WxPayClient.doV3Post(reqDto.getServiceUrl()
                , body
                , reqDto.getTimeoutMillis()
                , reqDto.getRetry()
                , apiV3Sign.signOfHeader(config.getMerchantId(), "POST"
                        , config.getServiceUrl(), body, context.getChannelConfigPo().getChannelCertPo())
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
    public BaseResultContext<DefaultResult> parseResp(String resp) throws Throwable {
        WxServiceMerchantAddReceiverRespDto respDto = JSON.parseObject(resp
                , new TypeReference<WxServiceMerchantAddReceiverRespDto>() {
                });
        DefaultResult result = new DefaultResult();
        result.setSuccess(successOfOption(null));
        result.setChannelCode("200");
        result.setChannelRespMessage(null);
        result.setChannelOriginRespDto(respDto);
        result.setChannelOriginResponse(resp);
        return result;
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
