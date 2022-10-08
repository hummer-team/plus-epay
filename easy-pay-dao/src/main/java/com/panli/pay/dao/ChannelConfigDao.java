package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.ChannelConfigPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface ChannelConfigDao {
    int insert(@Param("po")ChannelConfigPo po);
    ChannelConfigPo queryByCode(@Param("channelCode")String channelCode);
}
