/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.panli.pay.service.domain.core;

import com.hummer.common.exceptions.AppException;
import com.panli.pay.dao.ChannelConfigDao;
import com.panli.pay.dao.ChannelPlatformConfigDao;
import com.panli.pay.service.domain.context.BaseContext;
import com.panli.pay.service.domain.context.BaseResultContext;
import com.panli.pay.service.domain.enums.ChannelActionEnum;
import com.panli.pay.support.model.po.ChannelConfigPo;
import com.panli.pay.support.model.po.ChannelPlatformConfigPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractTemplate {
    @Autowired
    private ChannelConfigDao channelConfigDao;
    @Autowired
    private ChannelPlatformConfigDao platformConfigDao;

    /**
     * return channel config and check is valid
     *
     * @param platformCode platform code
     * @param channelCode  channel code
     * @return
     */
    protected ChannelConfigPo checkChannelIsValid(String platformCode, String channelCode, ChannelActionEnum typeEnum) {
        ChannelPlatformConfigPo platformConfigPo =
                platformConfigDao.queryByCode(platformCode, channelCode);

        if (platformConfigPo == null) {
            throw new AppException(40007, String.format("platform valid channel code. %s - %s", platformCode
                    , channelCode));
        }

        ChannelConfigPo channelConfigPo;
        switch (typeEnum) {
            case PAY:
            case PROFIT_SHARING_REQUEST:
            case WX_SERVICE_MCH_BAR_CODE_PAY:
                channelConfigPo = channelConfigDao.queryByCode(platformConfigPo.getChannelPayCode());
                break;
            case REFUND:
                channelConfigPo = channelConfigDao.queryByCode(platformConfigPo.getChannelRefundCode());
                break;
            case PAY_QUERY:
            case WX_SERVICE_MCH_BAR_CODE_QUERY:
                channelConfigPo = channelConfigDao.queryByCode(platformConfigPo.getChannelPayQueryCode());
                break;
            case REFUND_QUERY:
                channelConfigPo = channelConfigDao.queryByCode(platformConfigPo.getChannelRefundQueryCode());
                break;
            case CANCEL:
                channelConfigPo = channelConfigDao.queryByCode(platformConfigPo.getChannelCancelCode());
                break;
            default:
                throw new AppException(40008, "invalid channel action");
        }

        //assert is null
        if (channelConfigPo == null) {
            throw new AppException(40009, "channel config invalid channel code --> " + channelCode
                    , channelCode);
        }

        return channelConfigPo;
    }


    protected void freeResourceForPay(BaseContext<? extends BaseContext<?>> reqContext
            , BaseResultContext<? extends BaseResultContext<?>> result) {
        Optional.ofNullable(reqContext.getAffixData()).ifPresent(Map::clear);
        reqContext = null;
        result = null;
        log.debug("free resource reqContext,resultContext");
    }
}
