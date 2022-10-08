package com.panli.pay.service.domain.template;

import com.hummer.common.utils.DateUtil;
import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.dao.ProfitSharingReceiversMapper;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.result.BaseResultContext;
import com.panli.pay.service.domain.context.ProfitSharingAddReceiverContext;
import com.panli.pay.service.domain.result.ProfitSharingAddReceiverResultContext;
import com.panli.pay.service.domain.core.AbstractProfitSharingAddReceiverTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.ChannelPlatformConfigPo;
import com.panli.pay.support.model.po.ProfitSharingReceiversPo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_ADD_RECEIVER_TEMPLATE;

/**
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/9/29 14:06
 */
@Service(DEFAULT_PROFIT_SHARING_ADD_RECEIVER_TEMPLATE)
public class DefaultProfitSharingAddReceiverTemplate extends AbstractProfitSharingAddReceiverTemplate {
    @Autowired
    private ProfitSharingReceiversMapper profitSharingReceiversMapper;

    @Override
    protected void setContextAndSelfCheck(BaseContext<? extends BaseContext<?>> context) {
        ChannelConfigPo config = checkChannelIsValid(context.getPlatformCode(), context.getChannelCode()
                , ChannelActionEnum.ADD_RECEIVER);
        context.setChannelConfigPo(config);
        context.setRequestId(MDC.get(REQUEST_ID));
    }

    @Override
    protected BaseResultContext<ProfitSharingAddReceiverResultContext> checkExists(BaseContext<? extends BaseContext<?>> context) {
        ChannelConfigPo config = context.getChannelConfigPo();
        // valid repeat
        ProfitSharingAddReceiverContext context2 = (ProfitSharingAddReceiverContext) context;
        ChannelPlatformConfigPo platformConfig = config.getPlatformConfig();
        ProfitSharingReceiversPo receiversPo = profitSharingReceiversMapper.queryByUnique(platformConfig.getChannelPlatform(),
                config.getMerchantId(), context2.getSubMchId(), context2.getAccount());
        if (receiversPo != null) {
            return ObjectCopyUtils.copy(context2, ProfitSharingAddReceiverResultContext.class);
        }
        return null;
    }

    @Override
    protected void checkRisk(BaseContext<? extends BaseContext<?>> context) {

    }

    @Override
    protected void handleResult(BaseContext<? extends BaseContext<?>> context, BaseResultContext<ProfitSharingAddReceiverResultContext> result) {
        if (!result.isSuccess()) {
            return;
        }
        ProfitSharingReceiversPo po = new ProfitSharingReceiversPo();
        ChannelConfigPo config = context.getChannelConfigPo();
        ProfitSharingAddReceiverContext context2 = (ProfitSharingAddReceiverContext) context;
        ChannelPlatformConfigPo platformConfig = config.getPlatformConfig();
        po.setChannelPlatform(platformConfig.getChannelPlatform());
        po.setCreatedDateTime(DateUtil.now());
        po.setCreatedUserId(context2.getUserId());
        po.setIsDeleted(false);
        po.setReceiverAccount(context2.getAccount());
        po.setReceiverType(context2.getType());
        po.setServiceMchId(config.getMerchantId());
        po.setSubMchId(context2.getSubMchId());
        profitSharingReceiversMapper.insert(po);
    }
}
