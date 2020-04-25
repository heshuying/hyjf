package com.hyjf.mqreceiver.coupon.loans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.loans.CouponLoansBean;
import com.hyjf.coupon.loans.CouponLoansService;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.rabbitmq.client.Channel;

public class CouponLoansHjhMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(CouponLoansHjhMessageHadnle.class);

    @Autowired
    CouponLoansService couponLoansService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------汇计划优惠券放款开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【汇计划优惠券放款】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        String msgBody = new String(message.getBody());
        
        _log.info("【汇计划优惠券放款】接收到的消息：" + msgBody);
        
        CouponLoansBean loansBean;
        try {
            loansBean = JSONObject.parseObject(msgBody, CouponLoansBean.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(loansBean.getOrderId()) && Validator.isNull(loansBean.getOrderIdCoupon())) {
            _log.error("【汇计划优惠券放款】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String redisKey = "couponloanshjh:" + (Validator.isNull(loansBean.getOrderId())? loansBean.getOrderIdCoupon() : loansBean.getOrderId());
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【汇计划优惠券放款】当前标的正在放款....");
            return;
        }
        
        List<BorrowTenderCpn> listTenderCpn = new ArrayList<BorrowTenderCpn>();
        if(StringUtils.isNotEmpty(loansBean.getOrderId())){
        	listTenderCpn = couponLoansService.getBorrowTenderCpnHjhList(loansBean.getOrderId());
        }else if(StringUtils.isNotEmpty(loansBean.getOrderIdCoupon())){
        	listTenderCpn = couponLoansService.getBorrowTenderCpnHjhCouponOnlyList(loansBean.getOrderIdCoupon());
        }
        
        /** 循环优惠券出借详情列表 */
        for (BorrowTenderCpn borrowTenderCpn : listTenderCpn) {
            try {
                if (Validator.isNull(borrowTenderCpn.getLoanOrdid())) {
                    // 设置放款订单号
                    borrowTenderCpn.setLoanOrdid(GetOrderIdUtils.getOrderId2(borrowTenderCpn
                            .getUserId()));
                    // 设置放款时间
                    borrowTenderCpn.setOrderDate(GetOrderIdUtils.getOrderDate());
                    // 更新放款信息
                    couponLoansService.updateBorrowTenderCpn(borrowTenderCpn);
                }
                _log.info("【汇计划优惠券放款】优惠券使用订单号：" + borrowTenderCpn.getNid());
                
                List<Map<String, String>> msgList =
                        couponLoansService.updateCouponRecoverHjh(borrowTenderCpn, loansBean.getOrderId());
                if (msgList != null && msgList.size() > 0) {
                    // 发送短信
                    couponLoansService.sendSmsCoupon(msgList);
                    // 发送push消息
                    System.out.println("--------------准备调用sendAppMSCoupon方法推送push消息--------");
                    couponLoansService.sendAppMSCoupon(msgList);
                    System.out.println("--------------调用sendAppMSCoupon方法发送push消息结束");
                }
            } catch (Exception e) {
                _log.error("---优惠券放款异常。。。"+"标的编号："+borrowTenderCpn.getBorrowNid() + " 优惠券使用订单号：" + borrowTenderCpn.getNid(), e);
            }
        }
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
         RedisUtils.del(redisKey);
         
        _log.info("----------------------------汇计划优惠券放款结束--------------------------------" + this.toString());
    }
}
