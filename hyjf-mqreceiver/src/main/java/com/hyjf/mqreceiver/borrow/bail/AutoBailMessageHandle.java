package com.hyjf.mqreceiver.borrow.bail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.bail.AutoBailService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.rabbitmq.client.Channel;

/**
 * 自动审核保证金消息监听器
 * @author liushouyi
 *
 */
@Component(value="autoBailMessageHandle")
public class AutoBailMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(AutoBailMessageHandle.class);

    @Autowired
    AutoBailService autoBailService;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
    	// --> 消息检查
        if(message == null || message.getBody() == null){
            _log.error("【自动审核保证金】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
       // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【自动审核保证金】接收到的消息：" + msgBody);
        
        HjhPlanAsset mqHjhPlanAsset;
        try {
        	mqHjhPlanAsset = JSONObject.parseObject(msgBody, HjhPlanAsset.class);
            if(mqHjhPlanAsset == null || mqHjhPlanAsset.getBorrowNid() == null){
            	 _log.info("解析为空：" + msgBody);
            	channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
       // --> 消息处理
        
        
        try {
			
			// 自动审核保证金
			_log.info(mqHjhPlanAsset.getAssetId()+" 开始自动审核保证金 "+ mqHjhPlanAsset.getInstCode());
			Borrow borrow = this.autoBailService.getBorrowByBorrowNid(mqHjhPlanAsset.getBorrowNid());
			if(borrow == null){
				_log.info(mqHjhPlanAsset.getBorrowNid()+" 该资产在表里不存在！！");
				return;
			}
	        
			// redis 放重复检查
	        String redisKey = "borrowbail:" + borrow.getInstCode()+borrow.getBorrowNid();
	        boolean result = RedisUtils.tranactionSet(redisKey, 300);
	        if(!result){
	            _log.info(borrow.getInstCode()+" 正在自动审核保证金(redis) "+borrow.getBorrowNid());
	            return;
	        }
	        
			// 业务校验
			if(borrow.getStatus() != null && borrow.getStatus().intValue() == 1 && 
					borrow.getVerifyStatus() != null && borrow.getVerifyStatus().intValue() == 0){
				_log.info(mqHjhPlanAsset.getBorrowNid()+" 该标的状态为审核保证金状态、开始自动审核");
			} else {
				_log.info(mqHjhPlanAsset.getBorrowNid()+" 该标的状态不是审核保证金状态");
				return;
			}
			
			//判断该资产是否可以自动审核保证金
			HjhAssetBorrowType hjhAssetBorrowType = this.autoBailService.selectAssetBorrowType(borrow);
			boolean flag = this.autoBailService.updateRecordBorrow(borrow,hjhAssetBorrowType);
			if (!flag) {
				_log.error("自动审核保证金失败！" + "[资产编号：" + borrow.getBorrowNid() + "]");
			}else{
				this.autoBailService.sendToMQ(borrow,  RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
				_log.info(borrow.getBorrowNid()+" 成功发送到初审队列");
			}
			
			_log.info(borrow.getBorrowNid()+" 结束自动审核保证金");
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
        
    }
    
   
}
