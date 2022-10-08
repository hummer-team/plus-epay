package com.panli.pay.support.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProfitSharingReceiversPo {
    private Integer id;

    @ApiModelProperty("渠道")
    private String channelPlatform;

    @ApiModelProperty("服务商")
    private String serviceMchId;

    @ApiModelProperty("子商户")
    private String subMchId;

    @ApiModelProperty("类型")
    private String receiverType;

    @ApiModelProperty("分账接收商户")
    private String receiverAccount;

    @ApiModelProperty("创建人")
    private String createdUserId;

    @ApiModelProperty("创建时间")
    private Date createdDateTime;

    @ApiModelProperty("修改人")
    private String lastModifiedUserId;

    @ApiModelProperty("修改时间")
    private Date lastModifiedDatetime;

    @ApiModelProperty("是否删除")
    private Boolean isDeleted;

}