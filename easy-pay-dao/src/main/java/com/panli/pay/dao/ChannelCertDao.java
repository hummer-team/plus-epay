package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.ChannelCertPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface ChannelCertDao {

    ChannelCertPo selectByPrimaryKey(@Param("channelPlatform") String channelPlatform
            , @Param("merchantId") String merchantId);

    void updateById(ChannelCertPo po);
}