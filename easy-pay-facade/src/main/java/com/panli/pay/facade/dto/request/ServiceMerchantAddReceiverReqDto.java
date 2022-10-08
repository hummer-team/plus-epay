package com.panli.pay.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author edz
 */
@Data
public class ServiceMerchantAddReceiverReqDto {

    @ApiModelProperty(value = "渠道code", required = true)
    @NotEmpty(message = "channelCode can not empty")
    private String channelCode;

    private String platformCode;

    private String subAppId;

    @ApiModelProperty(value = "分账方mchId", required = true)
    @NotEmpty(message = "sub mch id can not empty")
    private String subMchId;

    @ApiModelProperty(value = "接收账户类型，MERCHANT_ID", required = true)
    @NotEmpty(message = "type can not empty")
    private String type;

    @ApiModelProperty(value = "接收商户号", required = true)
    @NotEmpty(message = "account can not empty")
    private String account;

    @ApiModelProperty("接收方姓名")
    private String name;

    @ApiModelProperty(value = "SERVICE_PROVIDER 服务商,STORE_OWNER 店主,DISTRIBUTOR 分销商", required = true)
    @NotEmpty(message = "relationType can not empty")
    private String relationType;

    @ApiModelProperty("子商户与接收方具体的关系")
    private String customRelation;
}
