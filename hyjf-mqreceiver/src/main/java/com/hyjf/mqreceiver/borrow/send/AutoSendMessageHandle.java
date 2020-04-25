package com.hyjf.mqreceiver.borrow.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.send.AutoSendService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.rabbitmq.client.Channel;

/**
 * 自动录标消息监听器
 * @author dxj
 *
 */
@Component(value="autoSendMessageHandle")
public class AutoSendMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(AutoSendMessageHandle.class);

    @Autowired
    AutoSendService autoSendService;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
    	// --> 消息检查
        if(message == null || message.getBody() == null){
            _log.error("【自动录标任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
       // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【自动录标请求】接收到的消息：" + msgBody);
        
        HjhPlanAsset mqHjhPlanAsset;
        try {
        	mqHjhPlanAsset = JSONObject.parseObject(msgBody, HjhPlanAsset.class);
            if(mqHjhPlanAsset == null || mqHjhPlanAsset.getAssetId() == null){
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
			
			// 资产自动录标
			_log.info(mqHjhPlanAsset.getAssetId()+" 开始自动录标 "+ mqHjhPlanAsset.getInstCode());
			HjhPlanAsset hjhPlanAsset = this.autoSendService.selectPlanAsset(mqHjhPlanAsset.getAssetId(), mqHjhPlanAsset.getInstCode());
			if(hjhPlanAsset == null){
				_log.info(mqHjhPlanAsset.getAssetId()+" 该资产在表里不存在！！");
				return;
			}
	        
			// redis 放重复检查
	        String redisKey = "borrowsend:" + hjhPlanAsset.getInstCode()+hjhPlanAsset.getAssetId();
	        boolean result = RedisUtils.tranactionSet(redisKey, 300);
	        if(!result){
				_log.info(hjhPlanAsset.getInstCode()+" 正在录标 (redis)"+hjhPlanAsset.getAssetId());
				return;
			}
	        
			// 业务校验
			if(hjhPlanAsset.getStatus() != null && hjhPlanAsset.getStatus().intValue() != 0 && 
					hjhPlanAsset.getVerifyStatus() != null && hjhPlanAsset.getVerifyStatus().intValue() == 1){
				_log.info(mqHjhPlanAsset.getAssetId()+" 该资产状态不是录标状态");
				return;
			}
			
			
			//判断该资产是否可以自动录标，是否关联计划
			HjhAssetBorrowType hjhAssetBorrowType = this.autoSendService.selectAssetBorrowType(hjhPlanAsset);
			if(hjhAssetBorrowType==null || hjhAssetBorrowType.getAutoAdd() != 1){
				_log.info(hjhPlanAsset.getAssetId()+" 该资产不能自动录标,流程配置未启用");
				return;
			}
			
			boolean flag = this.autoSendService.insertSendBorrow(hjhPlanAsset,hjhAssetBorrowType);
			if (!flag) {
				_log.info("自动录标失败！" + "[资产编号：" + hjhPlanAsset.getAssetId() + "]");
			}else{
				// 成功后到备案队列
				this.autoSendService.sendToMQ(hjhPlanAsset, RabbitMQConstants.ROUTINGKEY_BORROW_RECORD);
				
			}
			
			_log.info(hjhPlanAsset.getAssetId()+" 结束自动录标");
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
        
    }
    
   
}
