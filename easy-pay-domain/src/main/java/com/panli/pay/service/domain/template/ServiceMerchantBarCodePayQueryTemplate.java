package com.panli.pay.service.domain.template;

import com.panli.pay.facade.dto.request.BaseQueryPaymentStatusRequestDto;
import com.panli.pay.facade.dto.request.ServiceMerchantBarCodePayQueryRequestDto;
import com.panli.pay.service.domain.context.PaymentQueryContext;
import org.springframework.stereotype.Service;

import static com.panli.pay.service.domain.enums.ConstantDefine.SERVICE_MERCHANT_BAR_CODE_PAY_QUERY_TEMPLATE;

/**
 * @author edz
 */
@Service(SERVICE_MERCHANT_BAR_CODE_PAY_QUERY_TEMPLATE)
public class ServiceMerchantBarCodePayQueryTemplate extends DefaultPaymentQueryTemplate {

    @Override
    protected PaymentQueryContext setContextAndSelfCheck(
            BaseQueryPaymentStatusRequestDto<? extends BaseQueryPaymentStatusRequestDto<?>> dto) {

        PaymentQueryContext queryContext = super.setContextAndSelfCheck(dto);
        ServiceMerchantBarCodePayQueryRequestDto reqDto = (ServiceMerchantBarCodePayQueryRequestDto) dto.getData();
        queryContext.setSubAppId(reqDto.getSubAppId());
        queryContext.setSubMchId(reqDto.getSubMchId());
        return queryContext;
    }
}
