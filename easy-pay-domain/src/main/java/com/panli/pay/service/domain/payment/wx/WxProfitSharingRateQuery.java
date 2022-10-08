package com.panli.pay.service.domain.payment.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.integration.wxpayment.WxPayClient;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.ProfitSharingRateQueryContext;
import com.panli.pay.service.domain.result.ProfitSharingRateQueryResult;
import com.panli.pay.service.domain.core.PaymentChannel;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.payment.wx.context.req.WxProfitSharingRateQueryReqDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.panli.pay.service.domain.enums.ConstantDefine.WX_FZ_RATE_QUERY;

/**
 * @author edz
 * @see <a href='https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml'>请求分账</a>
 */
@Service(WX_FZ_RATE_QUERY)
@Slf4j
public class WxProfitSharingRateQuery extends BaseV3Payment implements PaymentChannel<WxProfitSharingRateQueryReqDto
        , String, ProfitSharingRateQueryContext, ProfitSharingRateQueryResult> {
    @Autowired
    private ApiSign apiV3Sign;

    /**
     * builder request body
     *
     * @param context context
     * @return
     */
    @Override
    public WxProfitSharingRateQueryReqDto builder(BaseContext<ProfitSharingRateQueryContext> context) throws Throwable {

        WxProfitSharingRateQueryReqDto reqDto = ObjectCopyUtils.copy(context, WxProfitSharingRateQueryReqDto.class);
        reqDto.setServiceUrl(context.getChannelConfigPo().getServiceUrl().replace("{sub_mchid}",
                reqDto.getSubMchId()));
        reqDto.setTimeoutMillis(context.getChannelConfigPo().getConnectTimeoutMs());
        return reqDto;
    }

    /**
     * call channel service
     *
     * @return
     * @throws Throwable
     */
    @Override
    public String doCall(BaseContext<ProfitSharingRateQueryContext> context, WxProfitSharingRateQueryReqDto reqDto) throws Throwable {
        return WxPayClient.doGet(reqDto.getServiceUrl()
                , reqDto.getTimeoutMillis()
                , reqDto.getRetry()
                , apiV3Sign.signOfHeader(context.getChannelConfigPo().getMerchantId(), HttpMethod.GET.name()
                        , reqDto.getServiceUrl(), null, context.getChannelConfigPo().getChannelCertPo())
                , new BasicHeader("Accept", "application/json")
        );
    }

    /**
     * parse channel response to local context
     *
     * @param resp resp channel response raw content
     * @return {@link BaseResultContext}
     */
    @Override
    public BaseResultContext<ProfitSharingRateQueryResult> parseResp(String resp) throws Throwable {
        ProfitSharingRateQueryResult result = JSON.parseObject(resp
                , new TypeReference<ProfitSharingRateQueryResult>() {
                });
        result.setSuccess(successOfOption(null));
        result.setChannelCode("200");
        result.setChannelRespMessage(null);
        result.setChannelOriginRespDto(result);
        result.setChannelOriginResponse(resp);
        result.setResult(result);
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
