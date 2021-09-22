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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hummer.common.http.HttpResult;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.facade.dto.request.ProfitSharingOrderCreateRequestDto;
import com.panli.pay.facade.dto.response.BasePaymentResp;
import com.panli.pay.facade.dto.response.ProfitSharingOrderCreateRespDto;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.context.ProfitSharingContext;
import com.panli.pay.service.domain.context.ProfitSharingResultContext;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.ProfitSharingStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxProfitSharingReqContext;
import com.panli.pay.service.domain.payment.wx.context.resp.WxProfitSharingOrderCreateRespDto;
import com.panli.pay.service.domain.payment.wx.context.resp.WxResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_REQUEST;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml'>请求分账</a>
 */
@Service(WX_FZ_REQUEST)
@Slf4j
public class WxProfitSharingRequest extends BaseV3Payment implements PaymentChannel<WxProfitSharingReqContext
        , HttpResult, ProfitSharingContext, ProfitSharingResultContext> {
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
        req.setSubMchId(dto.getSubMchId());
        req.setUnfreezeUnSplit(dto.getUnfreezeUnSplit());
        req.setReceivers(ObjectCopyUtils.copyByList(dto.getReceivers(), WxProfitSharingReqContext.Receivers.class));
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
    public HttpResult doCall(BaseContext<ProfitSharingContext> context, WxProfitSharingReqContext reqContext) throws Throwable {
        String body = JSON.toJSONString(reqContext);
        return WxPayClient.doPostWith(reqContext.getServiceUrl()
                , body
                , ContentType.APPLICATION_JSON.withCharset("UTF-8").toString()
                , reqContext.getTimeoutMillis()
                , reqContext.getRetry()
                , apiV3Sign.signOfHeader(reqContext.getSubMchId(), "POST"
                        , reqContext.getServiceUrl(), body)
                , new BasicHeader("Accept", "application/json"));
    }

    /**
     * parse channel response to local context
     *
     * @param resp resp channel response raw content
     * @return {@link BaseResultContext}
     */
    @Override
    public BaseResultContext<ProfitSharingResultContext> parseResp(HttpResult resp) throws Throwable {
        WxProfitSharingOrderCreateRespDto respDto = JSON.parseObject(resp.getResult()
                , new TypeReference<WxProfitSharingOrderCreateRespDto>() {

                });

        WxResponse response = successOfHttpStatus(resp.getStatus());

        ProfitSharingResultContext context = ProfitSharingResultContext.builder()
                .channelFzOrderId(respDto.getOrderId())
                .success(response.isSuccess())
                .status(ProfitSharingStatusEnum.getByChannelCode(respDto.getState()))
                .channelCode(response.getCode())
                .channelRespMessage(response.getMessage())
                .channelOriginResponse(resp.getResult())
                .channelOriginRespDto(respDto)
                .build();
        context.setResult(context);

        return context;
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
            BaseResultContext<ProfitSharingResultContext> result, HttpResult channelResp) {
        WxProfitSharingOrderCreateRespDto originDto = result.convertOriginRespDto();

        ProfitSharingOrderCreateRespDto respDto = new ProfitSharingOrderCreateRespDto();
        respDto.setChannelFzOrderId(originDto.getOrderId());
        respDto.setStatus(result.getResult().getStatus().ordinal());
        respDto.setStatusDesc(result.getResult().getStatus().getDescribe());
        respDto.setReceivers(ObjectCopyUtils.copyByList(originDto.getReceivers(), ProfitSharingOrderCreateRespDto.Receivers.class));

        return respDto;
    }
}
