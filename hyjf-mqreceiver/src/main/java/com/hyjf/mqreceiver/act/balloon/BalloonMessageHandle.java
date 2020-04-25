package com.hyjf.mqreceiver.act.balloon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.act.act2017.balloon.ActBalloonService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.validator.Validator;
import com.rabbitmq.client.Channel;

public class BalloonMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BalloonMessageHandle.class);

    @Autowired
    ActBalloonService balloonService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------双十二气球活动开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【双十二气球活动】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【双十二气球活动】接收到的消息：" + msgBody);
        
        JSONObject requestJson;
        try {
        	requestJson = JSONObject.parseObject(msgBody);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(requestJson.get("tenderNid")) || Validator.isNull(requestJson.get("tenderType"))) {
            _log.error("【双十二气球活动】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String tenderNid = (String)requestJson.get("tenderNid");
        String tenderType = (String)requestJson.get("tenderType");
        
        String redisKey = "actballoon:" + tenderNid;
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【双十二气球活动】正在处理....");
            return;
        }
        
        try {
        	balloonService.balloonTenderProcess(tenderNid, tenderType);
		} catch (Exception e) {
			_log.error("【双十二气球活动】处理失败，tenderNid："  + tenderNid + " tenderType:" + tenderType, e);
		}
        
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
         RedisUtils.del(redisKey);
         
        _log.info("----------------------------双十二气球活动结束 (tenderNid: " + tenderNid + ")--------------------------------" + this.toString());
    }
    
}
