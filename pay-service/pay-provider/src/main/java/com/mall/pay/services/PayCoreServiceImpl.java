package com.mall.pay.services;

import com.alibaba.fastjson.JSON;
import com.mall.pay.biz.abs.BasePayment;
import com.mall.pay.utils.ExceptionProcessorUtils;
import com.mall.pay.PayCoreService;
import com.mall.pay.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *  ciggar
 * create-date: 2019/7/30-13:54
 */
@Slf4j
@Component
@Service(cluster = "failover",timeout = 2000)
public class PayCoreServiceImpl implements PayCoreService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse execPay(PaymentRequest request) {

        PaymentResponse paymentResponse=new PaymentResponse();
        try {
            paymentResponse=BasePayment.paymentMap.get(request.getPayChannel()).process(request);
        }catch (Exception e){
            log.error("PayCoreServiceImpl.execPay Occur Exception :"+e);
            ExceptionProcessorUtils.wrapperHandlerException(paymentResponse,e);
        }
        return paymentResponse;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentNotifyResponse paymentResultNotify(PaymentNotifyRequest request) {
        log.info("paymentResultNotify request:{}", JSON.toJSONString(request));
        PaymentNotifyResponse response=new PaymentNotifyResponse();
        try{
            response=BasePayment.paymentMap.get
                    (request.getPayChannel()).completePayment(request);

        }catch (Exception e){
            log.error("paymentResultNotify occur exception:"+e);
            ExceptionProcessorUtils.wrapperHandlerException(response,e);
        }finally {
            log.info("paymentResultNotify return result:{}",JSON.toJSONString(response));
        }
        return response;
    }

    /**
     * 执行退款
     * @param refundRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundResponse execRefund(RefundRequest refundRequest) {
        RefundResponse refundResponse=new RefundResponse();
        try {
            refundResponse=BasePayment.paymentMap.get(refundRequest.getPayChannel()).process(refundRequest);
        }catch (Exception e){
            log.error("PayCoreServiceImpl.execRefund Occur Exception :{}",e);
            ExceptionProcessorUtils.wrapperHandlerException(refundResponse,e);
        }
        return refundResponse;
    }
}
