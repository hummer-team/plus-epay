package com.panli.pay.service.domain.template;

import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.facade.dto.request.ProfitSharingRateReqDto;
import com.panli.pay.service.domain.context.ProfitSharingRateQueryContext;
import com.panli.pay.service.domain.result.ProfitSharingRateQueryResult;
import com.panli.pay.service.domain.core.AbstractChannelTemplate;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;
import static com.panli.pay.service.domain.enums.ConstantDefine.DEFAULT_PROFIT_SHARING_RATE_QUERY_TEMPLATE;

/**
 * @author chen wei
 * @version 1.0
 * <p>Copyright: Copyright (c) 2021</p>
 * @date 2021/9/29 14:06
 */
@Service(DEFAULT_PROFIT_SHARING_RATE_QUERY_TEMPLATE)
public class DefaultProfitSharingRateQueryTemplate extends AbstractChannelTemplate<ProfitSharingRateReqDto,
        ProfitSharingRateQueryContext, ProfitSharingRateQueryResult> {

    @Override
    protected ProfitSharingRateQueryContext setContextAndSelfCheck(ProfitSharingRateReqDto dto) {
        ProfitSharingRateQueryContext context = ObjectCopyUtils.copy(dto, ProfitSharingRateQueryContext.class);
        ChannelConfigPo config = checkChannelIsValid(dto.getPlatformCode(), dto.getChannelCode()
                , ChannelActionEnum.PROFIT_SHARING_RATE_QUERY);
        context.setChannelConfigPo(config);
        context.setRequestId(MDC.get(REQUEST_ID));
        return context;
    }
}
