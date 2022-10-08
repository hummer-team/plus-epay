package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.ChannelPlatformConfigPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface ChannelPlatformConfigDao {
    int insert(@Param("po") ChannelPlatformConfigPo po);

    ChannelPlatformConfigPo queryByCode(@Param("platformCode") String platformCode
            , @Param("channelCode") String channelCode);
}
