package com.panli.pay.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.panli.pay.support.model.po.ProfitSharingReceiversPo;
import org.apache.ibatis.annotations.Param;

@DaoAnnotation
public interface ProfitSharingReceiversMapper {
    int insert(ProfitSharingReceiversPo record);

    ProfitSharingReceiversPo queryByUnique(@Param("channelPlatform") String channelPlatform,
                                            @Param("serviceMchId") String serviceMchId, @Param("subMchId") String subMchId,
                                            @Param("receiverAccount") String receiverAccount);

    int updateByPrimaryKeySelective(ProfitSharingReceiversPo record);
}