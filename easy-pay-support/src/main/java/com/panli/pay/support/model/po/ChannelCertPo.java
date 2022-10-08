package com.panli.pay.support.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelCertPo {

    private Integer id;

    @ApiModelProperty("商户id")
    private String merchantId;

    @ApiModelProperty("渠道平台WX ZFB")
    private String channelPlatform;

    @ApiModelProperty("证书")
    private String applicationCert;

    @ApiModelProperty("证书私钥")
    private String applicationKey;

    @ApiModelProperty("证书序列号")
    private String serialNo;

    @ApiModelProperty("证书平台公钥")
    private String certPlatformPub;

    @ApiModelProperty("获取token APi")
    private String tokenApiUrl;

    @ApiModelProperty("获取token APi method")
    private String tokenApiMethod;

    @ApiModelProperty("证书平台私钥")
    private String certPlatformPri;

    private String certPlatformSerialNo;

    @ApiModelProperty("证书过期时间")
    private Date certExpireTime;

    @ApiModelProperty("创建时间")
    private Date createdDateTime;

    @ApiModelProperty("修改时间")
    private Date lastModifiedDateTime;

    @ApiModelProperty("是否删除")
    private Boolean isDeleted;
}