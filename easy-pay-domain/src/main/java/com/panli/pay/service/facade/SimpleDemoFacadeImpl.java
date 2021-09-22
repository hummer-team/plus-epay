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

package com.panli.pay.service.facade;

import com.hummer.common.utils.ObjectCopyUtils;
import com.panli.pay.facade.dto.request.SimpleDemoSaveReqDto;
import com.panli.pay.dao.SimpleDemoDao;
import com.panli.pay.support.model.po.SimpleDemoPo;
import com.panli.pay.facade.SimpleDemoFacade;

import com.hummer.dao.annotation.TargetDataSourceTM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleDemoFacadeImpl implements SimpleDemoFacade {

    @Autowired
    private SimpleDemoDao simpleDemoDao;

    @Override
    @TargetDataSourceTM(dbName = "delivery_w"
            , transactionManager = "delivery_w_TM"
            , rollbackFor = Exception.class)
    public void save(SimpleDemoSaveReqDto batchDto) {
        log.debug("this is demo request , info {}",batchDto);
        //ignore
    }

    @Override
    public SimpleDemoSaveReqDto querySingleById(String id){
        SimpleDemoPo po =  simpleDemoDao.querySingleById(id);
        return ObjectCopyUtils.copy(po, SimpleDemoSaveReqDto.class);
    }
}