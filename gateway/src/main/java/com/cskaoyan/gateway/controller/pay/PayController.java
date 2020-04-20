package com.cskaoyan.gateway.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.gateway.form.pay.PayForm;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.user.annotation.Anoymous;
import com.mall.user.intercepter.TokenIntercepter;
import com.mall.pay.PayCoreService;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.dto.PaymentRequest;
import com.mall.pay.dto.PaymentResponse;
import com.mall.pay.dto.RefundRequest;
import com.mall.pay.dto.RefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * create by ciggar on 2020/04/05
 */
@Slf4j
@RestController
@RequestMapping("/cashier")
public class PayController {

    @Reference(timeout = 3000,retries = 0,check = false)
    PayCoreService payCoreService;

    @PostMapping("/pay")
    public ResponseData pay(@RequestBody PayForm payForm, HttpServletRequest httpServletRequest){
        log.info("支付表单数据:{}",payForm);
        PaymentRequest request=new PaymentRequest();
        String userInfo=(String)httpServletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        JSONObject object= JSON.parseObject(userInfo);
        Long uid=Long.parseLong(object.get("uid").toString());
        request.setUserId(uid);
        BigDecimal money=payForm.getMoney();
        request.setOrderFee(money);
        request.setPayChannel(payForm.getPayType());
//        request.setSubject(payForm.getInfo());
        //TODO 乱码问题临时解决方案
        request.setSubject("smartisan");
        request.setSpbillCreateIp("115.195.125.164");
        request.setTradeNo(payForm.getOrderId());
        request.setTotalFee(money);
        PaymentResponse response=payCoreService.execPay(request);
        if(response.getCode().equals(PayReturnCodeEnum.SUCCESS.getCode())){
            return new ResponseUtil<>().setData(response.getHtmlStr());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());

    }


    @PostMapping("/refund")
    public ResponseData refund(@RequestBody PayForm refundForm,HttpServletRequest httpServletRequest){
        log.info("订单退款入参:{}",JSON.toJSONString(refundForm));
        RefundRequest refundRequest=new RefundRequest();
        String userInfo=(String)httpServletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        JSONObject object= JSON.parseObject(userInfo);
        Long uid=Long.parseLong(object.get("uid").toString());
        refundRequest.setUserId(uid);
        refundRequest.setOrderId(refundForm.getOrderId());
        refundRequest.setRefundAmount(refundForm.getMoney());
        refundRequest.setPayChannel(refundForm.getPayType());
        RefundResponse refundResponse=payCoreService.execRefund(refundRequest);
        log.info("订单退款同步返回结果:{}",JSON.toJSONString(refundResponse));
        return new ResponseUtil<>().setData(refundResponse);
    }


}
