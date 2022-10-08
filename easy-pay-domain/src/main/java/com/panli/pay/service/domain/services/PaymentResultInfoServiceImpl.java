package com.panli.pay.service.domain.services;

import com.alibaba.fastjson.JSON;
import com.hummer.common.utils.DateUtil;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.core.PropertiesContainer;
import com.hummer.dao.annotation.TargetDataSourceTM;
import com.panli.pay.dao.AmountFlowDao;
import com.panli.pay.dao.PayCallRecordDao;
import com.panli.pay.dao.PaymentOrderDao;
import com.panli.pay.service.domain.constant.Constants;
import com.panli.pay.service.domain.context.PaymentContext;
import com.panli.pay.service.domain.context.RefundContext;
import com.panli.pay.service.domain.enums.OrderTypeEnum;
import com.panli.pay.service.domain.enums.PaymentStatusEnum;
import com.panli.pay.service.domain.result.PaymentResultContext;
import com.panli.pay.service.domain.result.RefundResultContext;
import com.panli.pay.support.model.po.AmountFlowPo;
import com.panli.pay.support.model.po.PayCallRecordPo;
import com.panli.pay.support.model.po.PaymentOrderPo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hummer.common.SysConstant.REQUEST_ID;

@Service
public class PaymentResultInfoServiceImpl implements PaymentResultInfoService {
    @Autowired
    private PaymentOrderDao paymentOrderDao;
    @Autowired
    private PayCallRecordDao recordDao;
    @Autowired
    private AmountFlowDao amountFlowDao;

    @Override
    public void saveProfitSharingResult(PaymentResultContext resultContext
            , PaymentContext paymentContext
            , String reqBody) {
        addPayFlowRecord(resultContext, paymentContext, reqBody);
    }

    public void addPayFlowRecord(PaymentResultContext resultContext, PaymentContext paymentContext, String reqBody) {
        PayCallRecordPo recordPo = ObjectCopyUtils.copy(resultContext, PayCallRecordPo.class);
        recordPo.setPlatformCode(paymentContext.getPlatformCode());
        recordPo.setChannelCode(paymentContext.getChannelCode());
        recordPo.setBusinessCode(paymentContext.getPlatformSubType());
        recordPo.setCallCostTimeMills(resultContext.getCostTimeMills());
        recordPo.setOriginRespBody(StringUtils.substring(resultContext.getChannelOriginResponse()
                , 0
                , PropertiesContainer.valueOfInteger("channel.origin.resp.max.length", (int) Short.MAX_VALUE)));
        recordPo.setRequestBody(reqBody);
        recordPo.setRequestId(MDC.get(REQUEST_ID));
        recordPo.setBatchId(paymentContext.getBatchId());
        recordPo.setTradeId(paymentContext.getTradeId());
        recordPo.setBatchId(paymentContext.getBatchId());

        recordDao.insert(recordPo);
    }

    @Override
    @TargetDataSourceTM(dbName = Constants.DbParams.PURCHASE_DB_NAME
            , transactionManager = Constants.DbParams.PURCHASE_DB_TM, rollbackFor = Throwable.class)
    public void savePaymentResult(PaymentResultContext resultContext
            , PaymentContext paymentContext
            , String reqBody
            , boolean createRecord, boolean createOrder, boolean createPayFlow) {
        if (createRecord) {
            addPayFlowRecord(resultContext, paymentContext, reqBody);
        }
        String subMchId = (String) paymentContext.getContext().getAffixData().get("subMchId");
        String merchantId = StringUtils.isNoneEmpty(subMchId) ? subMchId : paymentContext.getChannelConfigPo().getMerchantId();
        if (createOrder) {
            PaymentOrderPo orderPo = new PaymentOrderPo();
            orderPo.setAmount(paymentContext.getAmount());
            //default 0
            orderPo.setPlatformSubType(paymentContext.getPlatformSubType());
            orderPo.setTradeId(paymentContext.getTradeId());
            orderPo.setPaymentUserId(paymentContext.getUserId());
            orderPo.setChannelTradeId(resultContext.getChannelTradeId());
            orderPo.setChannelAdvancePaymentId(resultContext.getChannelAdvancePaymentId());

            orderPo.setPayChannelType(paymentContext.getPayChannelType().getCode());
            orderPo.setPlatformCode(paymentContext.getPlatformCode());
            orderPo.setRequestId(MDC.get(REQUEST_ID));
            orderPo.setStatusCode(resultContext.getPaymentStatus().getCode());
            orderPo.setPaymentDateTime(resultContext.getPaymentDateTime());
            orderPo.setChannelCode(paymentContext.getChannelCode());
            orderPo.setOrderTag(paymentContext.getOrderTag());
            orderPo.setRefundBatchId(paymentContext.getBatchId());
            orderPo.setMerchantId(merchantId);
            orderPo.setOrderType(paymentContext.getOrderType().ordinal());
            orderPo.setPayType(paymentContext.getPayChannelType().getCode());
            paymentOrderDao.insertPayOrder(orderPo);
        }
        if (createPayFlow) {
            AmountFlowPo po = new AmountFlowPo();
            po.setAmount(paymentContext.getAmount());
            po.setMerchantId(merchantId);
            po.setAmountUnitType(paymentContext.getAmountUnitType());
            po.setBatchId(null);
            po.setChannelTradeId(resultContext.getChannelTradeId());
            po.setCreatedDatetime(DateUtil.now());
            po.setFlowType(false);
            po.setRate(1d);
            po.setRequestId(MDC.get(REQUEST_ID));
            po.setTradeId(paymentContext.getTradeId());
            amountFlowDao.insert(po);
        }
    }

    @Override
    public void saveRefundResult(RefundResultContext context, RefundContext refundContext, String reqBody) {
        PayCallRecordPo recordPo = new PayCallRecordPo();
        recordPo.setPlatformCode(refundContext.getPlatformCode());
        recordPo.setChannelCode(refundContext.getChannelCode());
        recordPo.setBusinessCode(refundContext.getPlatformSubType());
        recordPo.setCallCostTimeMills(context.getCostTimeMills());
        recordPo.setOriginRespBody(StringUtils.substring(JSON.toJSONString(context.getChannelOriginResponse())
                , 0
                , PropertiesContainer.valueOfInteger("channel.origin.resp.max.length", (int) Short.MAX_VALUE)));
        recordPo.setRequestBody(reqBody);
        recordPo.setRequestId(MDC.get(REQUEST_ID));
        recordDao.insert(recordPo);

        if (!context.isSuccess()) {
            return;
        }
        PaymentOrderPo orderPo = new PaymentOrderPo();
        orderPo.setAmount(refundContext.getAmount());
        //default 0
        orderPo.setRefundBatchId(refundContext.getRefundBatchId());
        orderPo.setTradeId(refundContext.getTradeId());
        orderPo.setPaymentUserId(refundContext.getUserId());
        orderPo.setChannelTradeId(refundContext.getChannelTradeId());
        orderPo.setPlatformCode(refundContext.getPlatformCode());
        orderPo.setRequestId(MDC.get(REQUEST_ID));
        orderPo.setStatusCode(context.isSuccess()
                ? PaymentStatusEnum.REFUND_SUCCESS.getCode()
                : PaymentStatusEnum.REFUND_FAILED.getCode());
        orderPo.setChannelCode(refundContext.getChannelCode());
        orderPo.setOrderType(OrderTypeEnum.REFUND_ORDER.ordinal());
        //flowPo.setOrderTag(refundContext.get);
        paymentOrderDao.insertPayOrder(orderPo);
    }

    @Override
    public void saveRefundResultOfYugBean(RefundResultContext context, RefundContext refundContext) {
        PaymentOrderPo orderPo = refundContext.getPaymentOrder();
        assert orderPo != null;

        PaymentOrderPo refundOrderPo = new PaymentOrderPo();

        refundOrderPo.setRefundBatchId(refundContext.getRefundBatchId());
        refundOrderPo.setChannelCode(refundContext.getChannelCode());
        refundOrderPo.setTradeId(refundContext.getTradeId());
        refundOrderPo.setChannelTradeId(refundContext.getChannelTradeId());
        refundOrderPo.setAmount(refundContext.getAmount());
        refundOrderPo.setPlatformCode(refundContext.getPlatformCode());
        refundOrderPo.setChannelRefundId(context.getChannelRefundId());
        refundOrderPo.setPaymentUserId(refundContext.getUserId());
        refundOrderPo.setRequestId(MDC.get(REQUEST_ID));
        refundOrderPo.setStatusCode(context.getStatus().getCode());
        refundOrderPo.setOrderTag(orderPo.getOrderTag());
        refundOrderPo.setPlatformSubType(orderPo.getPlatformSubType());
        refundOrderPo.setChannelTradeStatus(orderPo.getChannelTradeStatus());
        refundOrderPo.setChannelRefundStatus(context.getChannelRefundStatus());
        refundOrderPo.setOrderType(OrderTypeEnum.REFUND_ORDER.ordinal());
        refundOrderPo.setPayType(orderPo.getPayType());
        refundOrderPo.setMerchantId(orderPo.getMerchantId());
        refundOrderPo.setPaymentDateTime(orderPo.getPaymentDateTime());

        paymentOrderDao.insertRefundOrder(refundOrderPo);
    }
}
