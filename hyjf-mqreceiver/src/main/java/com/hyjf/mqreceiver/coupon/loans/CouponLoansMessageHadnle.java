package com.hyjf.mqreceiver.coupon.loans;

import java.util.List;
import java.util.Map;

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

public class CouponLoansMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(CouponLoansMessageHadnle.class);

    @Autowired
    CouponLoansService couponLoansService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------优惠券放款开始------------------------");
        if(message == null || message.getBody() == null){
            _log.error("【优惠券放款】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【优惠券放款】接收到的消息：" + msgBody);
        
        CouponLoansBean loansBean;
        try {
            loansBean = JSONObject.parseObject(msgBody, CouponLoansBean.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(loansBean.getBorrowNid())) {
            _log.error("【优惠券放款】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String redisKey = "couponloans:" + loansBean.getBorrowNid();
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【优惠券放款】当前标的正在放款....");
            return;
        }
        
        List<BorrowTenderCpn> listTenderCpn = couponLoansService.getBorrowTenderCpnList(loansBean.getBorrowNid());
        
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
                _log.info("【优惠券放款】优惠券使用orderId: " + borrowTenderCpn.getNid() + " borrowNid: " + loansBean.getBorrowNid());
                List<Map<String, String>> msgList =
                        couponLoansService.updateCouponRecover(borrowTenderCpn);
                if (msgList != null && msgList.size() > 0) {
                    // 发送短信
                    couponLoansService.sendSmsCoupon(msgList);
                    // 发送push消息
                    System.out.println("--------------准备调用sendAppMSCoupon方法推送push消息--------");
                    couponLoansService.sendAppMSCoupon(msgList);
                    System.out.println("--------------调用sendAppMSCoupon方法发送push消息结束");
                }
            } catch (Exception e) {
                _log.error("---优惠券放款异常。。。"+"标的编号："+loansBean.getBorrowNid() + " 优惠券使用orderId: " + borrowTenderCpn.getNid(), e);
            }
        }
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
         RedisUtils.del(redisKey);
         
        _log.info("----------------------------优惠券放款结束--------------------------------");
    }
    
}
