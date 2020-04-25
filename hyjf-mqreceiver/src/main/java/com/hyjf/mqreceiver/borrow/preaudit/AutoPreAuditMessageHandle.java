package com.hyjf.mqreceiver.borrow.preaudit;

import com.hyjf.bank.service.mq.MqService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.bank.service.borrow.preaudit.AutoPreAuditService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动初审消息监听器
 * @author dxj
 *
 */
@Component(value="autoPreAuditMessageHandle")
public class AutoPreAuditMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(AutoPreAuditMessageHandle.class);

    @Autowired
    AutoPreAuditService autoPreAuditService;

    @Autowired
	MqService mqService;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
    	// --> 消息检查
        if(message == null || message.getBody() == null){
            _log.error("【自动初审任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
       // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【自动初审请求】接收到的消息：" + msgBody);
        
        HjhPlanAsset mqHjhPlanAsset;
        try {
        	mqHjhPlanAsset = JSONObject.parseObject(msgBody, HjhPlanAsset.class);
			/*--------------upd by liushouyi HJH3 Start---------------------*/
            if(mqHjhPlanAsset == null || (mqHjhPlanAsset.getAssetId() == null && mqHjhPlanAsset.getBorrowNid() == null)){
    		/*--------------upd by liushouyi HJH3 End---------------------*/
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
			
        	/*--------------upd by liushouyi HJH3 Start--------------*/
        	// 手动推送标的
			if (StringUtils.isNotBlank(mqHjhPlanAsset.getBorrowNid())) {
				_log.info(mqHjhPlanAsset.getBorrowNid()+" 开始自动初审 "+ mqHjhPlanAsset.getInstCode());

				// redis 放重复检查
		        String redisKey = "borrowpreaudit:" + mqHjhPlanAsset.getInstCode()+mqHjhPlanAsset.getBorrowNid();
		        boolean result = RedisUtils.tranactionSet(redisKey, 300);
		        if(!result){
		            _log.info(mqHjhPlanAsset.getInstCode()+" 正在初审(redis) "+mqHjhPlanAsset.getBorrowNid());
		            return;
		        }
		        
				Borrow borrow = this.autoPreAuditService.getBorrowByBorrowNid(mqHjhPlanAsset.getBorrowNid());
				if(borrow == null){
					_log.info(mqHjhPlanAsset.getBorrowNid()+" 该标的在表里不存在！！");
					return;
				}
				
				// 业务校验
				if(borrow.getStatus() != null && borrow.getStatus().intValue() != 1 && 
						borrow.getVerifyStatus() != null && borrow.getVerifyStatus().intValue() != 1){
					_log.info(borrow.getBorrowNid()+" 该资产状态不是初审(已审核保证金)状态");
					return;
				}
				
				//判断该资产是否可以自动初审，是否关联计划
				HjhAssetBorrowType hjhAssetBorrowType = this.autoPreAuditService.selectAssetBorrowType(borrow);
				if(hjhAssetBorrowType == null || hjhAssetBorrowType.getAutoAudit() == null || hjhAssetBorrowType.getAutoAudit() != 1){
					_log.info(borrow.getBorrowNid()+" 标的不能自动初审,原因自动初审未配置");
					return;
				}
				
				boolean flag = this.autoPreAuditService.updateRecordBorrow(borrow);
				if (!flag) {
					_log.error("自动初审失败！" + "[项目编号：" + borrow.getBorrowNid() + "]");
				}else{
					if (borrow.getIsEngineUsed() == 1) {
						// 成功后到关联计划队列
						MQBorrow mqBorrow = new MQBorrow();
						mqBorrow.setBorrowNid(borrow.getBorrowNid());
						this.autoPreAuditService.sendToMQ(mqBorrow,  RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
					} else {
						// 散标修改redis：借款的borrowNid,account借款总额
						RedisUtils.set(borrow.getBorrowNid(), borrow.getAccount().toString());
					}
					// 发送发标成功的消息队列到互金上报数据
					Map<String, String> param = new HashMap<String, String>();
					param.put("borrowNid", borrow.getBorrowNid());
					param.put("userId",borrow.getUserId() + "");
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTING_DELAY_KEY, JSONObject.toJSONString(param));
				}
				
				_log.info(borrow.getBorrowNid()+" 结束自动初审");
				
			}
			/*--------------upd by liushouyi HJH3 End--------------*/
			
			if (StringUtils.isNotBlank(mqHjhPlanAsset.getAssetId())) {
				// 资产自动初审
				_log.info(mqHjhPlanAsset.getAssetId()+" 开始自动初审 "+ mqHjhPlanAsset.getInstCode());
				HjhPlanAsset hjhPlanAsset = this.autoPreAuditService.selectPlanAsset(mqHjhPlanAsset.getAssetId(), mqHjhPlanAsset.getInstCode());
				if(hjhPlanAsset == null){
					_log.info(mqHjhPlanAsset.getAssetId()+" 该资产在表里不存在！！");
					return;
				}
		        
				// redis 放重复检查
		        String redisKey = "borrowpreaudit:" + hjhPlanAsset.getInstCode()+hjhPlanAsset.getAssetId();
		        boolean result = RedisUtils.tranactionSet(redisKey, 300);
		        if(!result){
		            _log.info(hjhPlanAsset.getInstCode()+" 正在初审(redis) "+hjhPlanAsset.getAssetId());
		            return;
		        }
		        
				// 业务校验
				if(hjhPlanAsset.getStatus() != null && hjhPlanAsset.getStatus().intValue() != 5 && 
						hjhPlanAsset.getVerifyStatus() != null && hjhPlanAsset.getVerifyStatus().intValue() == 1){
					_log.info(mqHjhPlanAsset.getAssetId()+" 该资产状态不是初审状态");
					return;
				}
				
				//判断该资产是否可以自动初审，是否关联计划
				HjhAssetBorrowType hjhAssetBorrowType = this.autoPreAuditService.selectAssetBorrowType(hjhPlanAsset);
				boolean flag = this.autoPreAuditService.updateRecordBorrow(hjhPlanAsset,hjhAssetBorrowType);
				if (!flag) {
					_log.error("自动初审失败！" + "[资产编号：" + hjhPlanAsset.getAssetId() + "]");
				}else{
					// 成功后到关联计划队列
					MQBorrow mqBorrow = new MQBorrow();
					mqBorrow.setBorrowNid(hjhPlanAsset.getBorrowNid());
					this.autoPreAuditService.sendToMQ(mqBorrow,  RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
					// 发送发标成功的消息队列到互金上报数据
					Map<String, String> param = new HashMap<String, String>();
					param.put("borrowNid", hjhPlanAsset.getBorrowNid());
					param.put("userId",hjhPlanAsset.getUserId() + "");
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTING_DELAY_KEY, JSONObject.toJSONString(param));
				}
				_log.info(hjhPlanAsset.getAssetId()+" 结束自动初审");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
        
    }
    
   
}
