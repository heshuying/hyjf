package com.hyjf.mqreceiver.coupon.repay;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.repay.CouponRepayBean;
import com.hyjf.coupon.repay.CouponRepayService;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;

import java.util.List;

public class CouponRepayMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(CouponRepayMessageHadnle.class);

    @Autowired
    private CouponRepayService repayService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------直投类优惠券还款开始------------------------");
        if(message == null || message.getBody() == null){
            _log.error("【优惠券还款】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【优惠券还款】接收到的消息：" + msgBody);
        
        CouponRepayBean repayBean;
        try {
            repayBean = JSONObject.parseObject(msgBody, CouponRepayBean.class);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(repayBean.getBorrowNid()) || Validator.isNull(repayBean.getPeriodNow())) {
            _log.error("【优惠券还款】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String redisKey = "couponrepay:" + repayBean.getBorrowNid() + ":" + repayBean.getPeriodNow();
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【优惠券还款】当前标的正在还款....");
            return;
        }
        
        int failTimes = 0;
        List<CouponTenderCustomize> couponTenderList = repayService.getCouponTenderList(repayBean.getBorrowNid());
        for(CouponTenderCustomize ct:couponTenderList){
            try{
            	_log.info("【优惠券还款】优惠券使用订单号：" + ct.getOrderId() + " borrowNid:" + ct.getBorrowNid());
                repayService.updateCouponRecoverMoney(repayBean.getBorrowNid(),repayBean.getPeriodNow(), ct);
                // 避免给即信端造成请求压力导致还款失败
                Thread.sleep(500);
            }catch(CannotAcquireLockException e){
            	failTimes++;
            	_log.error("【优惠券还款】发生锁等待超时异常，当前锁等待超时次数：" + failTimes + " 优惠券出借编号:" +ct.getOrderId() + " borrowNid:" + ct.getBorrowNid() , e);
            }catch(Exception e){
                // 本次优惠券还款失败
                _log.error("【优惠券还款】直投类优惠券还款失败，优惠券出借编号："+ct.getOrderId() + " borrowNid:" + ct.getBorrowNid(), e);
                /*resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc(e.getMessage());
                return resultBean;*/
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
        }

         RedisUtils.del(redisKey);
         
         if(failTimes > 0){
        	 _log.info("【优惠券还款】锁等待超时次数：" + failTimes, "重新放回消息队列等待再次执行,borrowNid:" + repayBean.getBorrowNid());
        	 Thread.sleep(1000*5);
        	 channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
         }else{
        	 channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         }
         
        _log.info("----------------------------直投类优惠券还款结束--------------------------------");
    }
    
}
