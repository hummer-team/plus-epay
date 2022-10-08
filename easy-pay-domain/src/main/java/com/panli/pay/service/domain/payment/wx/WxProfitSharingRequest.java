package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hummer.common.utils.DateUtil;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.facade.dto.request.ProfitSharingOrderCreateRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.facade.dto.response.ProfitSharingOrderCreateRespDto;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.ProfitSharingContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.enums.ProfitSharingStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxProfitSharingReqContext;
import com.panli.pay.service.domain.payment.wx.context.resp.WxProfitSharingOrderCreateRespDto;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.result.ProfitSharingResultContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.stream.Collectors;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_REQUEST;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml'>请求分账</a>
 */
@Service(WX_FZ_REQUEST)
@Slf4j
public class WxProfitSharingRequest extends BaseV3Payment implements PaymentChannel<WxProfitSharingReqContext
        , String, ProfitSharingContext, ProfitSharingResultContext> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxProfitSharingReqContext builder(BaseContext<ProfitSharingContext> context) throws Throwable {
        ProfitSharingOrderCreateRequestDto dto = (ProfitSharingOrderCreateRequestDto) context.getAffixData().get("dto");
        Assert.notNull(dto, "ProfitSharingOrderCreateRequestDto is null!");

        WxProfitSharingReqContext req = new WxProfitSharingReqContext();
        req.setAppId(context.getChannelConfigPo().getAppId());
        req.setOutOrderNo(context.getTradeId());
        req.setMerchantId(context.getChannelConfigPo().getMerchantId());
        req.setSubMchId(dto.getSubMchId());
        req.setUnfreezeUnSplit(dto.getUnfreezeUnSplit());
        req.setReceivers(dto.getReceivers().stream().map(i -> {
            WxProfitSharingReqContext.Receivers r = ObjectCopyUtils.copy(i, WxProfitSharingReqContext.Receivers.class);
            r.setAmount(convertAmount(i.getAmount()));
            return r;
        }).collect(Collectors.toList()));
        Assert.isTrue(CollectionUtils.isNotEmpty(req.getReceivers()), "receivers can not null");

        req.setTransactionId(context.getPaymentOrder().getChannelTradeId());
        req.setServiceUrl(context.getChannelConfigPo().getServiceUrl());
        req.setTimeoutMillis(context.getChannelConfigPo().getConnectTimeoutMs());
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
    public String doCall(BaseContext<ProfitSharingContext> context, WxProfitSharingReqContext reqContext) throws Throwable {
        String body = JSON.toJSONString(reqContext);
        return WxPayClient.doV3Post(reqContext.getServiceUrl()
                , body
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getMerchantId(), "POST"
                        , reqContext.getServiceUrl(), body, context.getChannelConfigPo().getChannelCertPo())
        );
    }

    /**
     * parse channel response to local context
     *
     * @param resp resp channel response raw content
     * @return {@link BaseResultContext}
     */
    @Override
    public BaseResultContext<ProfitSharingResultContext> parseResp(String resp) throws Throwable {
        WxProfitSharingOrderCreateRespDto respDto = JSON.parseObject(resp
                , new TypeReference<WxProfitSharingOrderCreateRespDto>() {
                });

        ProfitSharingResultContext context = ProfitSharingResultContext.builder()
                .channelFzOrderId(respDto.getOrderId())
                .success(successOfOption(null))
                .status(ProfitSharingStatusEnum.getByChannelCode(respDto.getState()))
                .channelCode("200")
                .channelRespMessage(null)
                .channelOriginResponse(resp)
                .channelOriginRespDto(respDto)
                .channelOriginResponse(resp)
                .paymentDateTime(DateUtil.now())
                .build();
        context.setResult(context);
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

    /**
     * builder response message
     *
     * @param result      {@link BaseResultContext}
     * @param channelResp channel response message
     * @return response body is map
     */
    @Override
    public BasePaymentResp<ProfitSharingOrderCreateRespDto> builderRespMessage(
            BaseResultContext<ProfitSharingResultContext> result, String channelResp) {
        WxProfitSharingOrderCreateRespDto originDto = result.convertOriginRespDto();

        ProfitSharingOrderCreateRespDto respDto = new ProfitSharingOrderCreateRespDto();
        respDto.setChannelFzOrderId(originDto.getOrderId());
        respDto.setStatus(result.getResult().getStatus().ordinal());
        respDto.setStatusDesc(result.getResult().getStatus().getDescribe());
        respDto.setReceivers(ObjectCopyUtils.copyByList(originDto.getReceivers(), ProfitSharingOrderCreateRespDto.Receivers.class));
        respDto.setChannelTradeId(originDto.getOrderId());
        return respDto;
    }
}
