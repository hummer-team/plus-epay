package com.panli.pay.service.domain.template;

import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.facade.dto.request.ProfitSharingReturnReqDto;
import com.panli.pay.service.domain.context.ProfitSharingReturnContext;
import com.panli.pay.service.domain.core.AbstractChannelTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_RETURN_TEMPLATE;

/**
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/9/29 14:06
 */
@Service(DEFAULT_PROFIT_SHARING_RETURN_TEMPLATE)
public class DefaultProfitSharingReturnTemplate extends AbstractChannelTemplate<ProfitSharingReturnReqDto,
        ProfitSharingReturnContext, RefundResultContext> {

    @Override
    protected ProfitSharingReturnContext setContextAndSelfCheck(ProfitSharingReturnReqDto dto) {
        ProfitSharingReturnContext context = ObjectCopyUtils.copy(dto, ProfitSharingReturnContext.class);
        ChannelConfigPo config = checkChannelIsValid(dto.getPlatformCode(), dto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_RETURN);
        context.setChannelConfigPo(config);
        context.setRequestId(MDC.get(REQUEST_ID));
        return context;
    }
}
