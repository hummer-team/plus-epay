package com.panli.pay.service.domain.core;

import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.AppBusinessAssert;
import com.panli.pay.dao.ChannelCertDao;
import com.panli.pay.dao.ChannelConfigDao;
import com.panli.pay.dao.ChannelPlatformConfigDao;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.services.NameBuilderService;
import com.panli.pay.support.model.po.ChannelCertPo;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.ChannelPlatformConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractTemplate {
    @Autowired
    private ChannelConfigDao channelConfigDao;
    @Autowired
    private ChannelPlatformConfigDao platformConfigDao;
    @Autowired
    private ChannelCertDao channelCertDao;

    /**
     * return channel config and check is valid
     *
     * @param platformCode platform code
     * @param channelCode  channel code
     * @return
     */
    protected ChannelConfigPo checkChannelIsValid(String platformCode, String channelCode, ChannelActionEnum typeEnum) {
        ChannelPlatformConfigPo platformConfigPo =
                platformConfigDao.queryByCode(platformCode, channelCode);

        if (platformConfigPo == null) {
            throw new AppException(40007, String.format("platform valid channel code. %s - %s", platformCode
                    , channelCode));
        }

        String targetChannelCode = null;
        switch (typeEnum) {
            case PAY:
            case PROFIT_SHARING_REQUEST:
            case WX_SERVICE_MCH_BAR_CODE_PAY:
                targetChannelCode = platformConfigPo.getChannelPayCode();
                break;
            case REFUND:
                targetChannelCode = platformConfigPo.getChannelRefundCode();
                break;
            case PAY_QUERY:
            case WX_SERVICE_MCH_BAR_CODE_QUERY:
                targetChannelCode = platformConfigPo.getChannelPayQueryCode();
                break;
            case REFUND_QUERY:
                targetChannelCode = platformConfigPo.getChannelRefundQueryCode();
                break;
            case CANCEL:
                targetChannelCode = platformConfigPo.getChannelCancelCode();
                break;
            case ADD_RECEIVER:
                targetChannelCode = platformConfigPo.getChannelAddReceiver();
                break;
            default:
                targetChannelCode = NameBuilderService.formatChannelKey(channelCode, typeEnum);
                break;
        }
        AppBusinessAssert.isTrue(StringUtils.isNotEmpty(targetChannelCode), 40008, "invalid channel action");
        ChannelConfigPo channelConfigPo = channelConfigDao.queryByCode(targetChannelCode);
        //assert is null
        if (channelConfigPo == null) {
            throw new AppException(40009, "channel config invalid channel code --> " + channelCode
                    , channelCode);
        }
        ChannelCertPo certPo = channelCertDao.selectByPrimaryKey(platformConfigPo.getChannelPlatform()
                , channelConfigPo.getMerchantId());
        channelConfigPo.setChannelCertPo(certPo);
        channelConfigPo.setPlatformConfig(platformConfigPo);
        return channelConfigPo;
    }


    protected void freeResourceForPay(BaseContext<? extends BaseContext<?>> reqContext
            , BaseResultContext<? extends BaseResultContext<?>> result) {
        Optional.ofNullable(reqContext.getAffixData()).ifPresent(Map::clear);
        reqContext = null;
        result = null;
        log.debug("free resource reqContext,resultContext");
    }
}
