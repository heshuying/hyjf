package com.hyjf.mqreceiver.borrow.planrepayquit;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrowrepay.plan.BatchHjhBorrowRepayService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 计划类项目退出任务
 * @author cwyang
 *
 */
@Component(value="borrowRepayPlanQuitMessageHadnle")
public class BorrowRepayPlanQuitMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BorrowRepayPlanQuitMessageHadnle.class);

    @Autowired
    BatchHjhBorrowRepayService batchRepayService;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------计划退出任务开始----------------------");
        if(message == null || message.getBody() == null){
            _log.error("【计划退出任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        HjhAccede accede = null;
        try {
        	accede = JSONObject.parseObject(msgBody, HjhAccede.class);
            if(accede == null || StringUtils.isBlank(accede.getAccedeOrderId())){
            	channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        String accedeOrderId = accede.getAccedeOrderId();
		Integer orderStatus = accede.getOrderStatus();
		Integer creditCompleteFlag = accede.getCreditCompleteFlag();
		if (orderStatus == 2) {
			_log.info("--------------计划订单号："+ accedeOrderId +"，开始进入锁定期！------");
		}else if(orderStatus == 5 && creditCompleteFlag == 1){//计划退出中并且清算标示完成
			_log.info("--------------计划订单号："+ accedeOrderId +"，开始退出计划！------");
		}
        // 生成任务key 校验并发请求
        String redisKey = RedisConstants.PLAN_REPAY_TASK + ":" + accedeOrderId;
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            RedisUtils.srem(RedisConstants.HJH_LOCK_REPEAT,accedeOrderId);
            _log.error("计划订单号退出中....");
            return;
        }
        
        try{
        	if (orderStatus == 2) {//锁定计划
        		batchRepayService.updateLockRepayInfo(accedeOrderId);
    		}else if(orderStatus == 5 && creditCompleteFlag == 1){//退出计划 计划退出中并且清算标示完成
    			batchRepayService.updateQuitRepayInfo(accedeOrderId);
    		}
        }catch(Exception e){
        	// 消息队列指令不消费
        	e.printStackTrace();
            _log.error("计划还款系统异常....");
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        RedisUtils.srem(RedisConstants.HJH_LOCK_REPEAT,accedeOrderId);
		RedisUtils.del(redisKey);
		_log.info("----------------------------计划退出结束--------------------------------");
    }
    
}
