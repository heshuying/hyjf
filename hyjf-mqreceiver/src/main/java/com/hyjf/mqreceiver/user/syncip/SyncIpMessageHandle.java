package com.hyjf.mqreceiver.user.syncip;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.register.SyncIpService;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;

/**
 * 根据用户IP获取地址消息监听器
 * @author dxj
 *
 */
@Component(value="syncIpMessageHandle")
public class SyncIpMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(SyncIpMessageHandle.class);

    @Autowired
    SyncIpService syncIpService;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
    	// --> 消息检查
        if(message == null || message.getBody() == null){
            _log.error("【根据用户IP获取地址任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
       // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【根据用户IP获取地址请求】接收到的消息：" + msgBody);
        
        Users users;
        try {
        	users = JSONObject.parseObject(msgBody, Users.class);
            if(users == null || users.getUserId() == null || StringUtils.isEmpty(users.getRegIp())){
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
			
			_log.info(users.getUserId()+" 开始根据用户IP获取地址 "+ users.getRegIp());

			//用户IP获取地址
			UsersInfo userInfo = syncIpService.getIpInfo(users);
			
			int reuslt = syncIpService.updateIpInfo(users, userInfo);
			
			_log.info(users.getUserId()+" 结束根据用户IP获取地址 "+reuslt);
			
			// 淘宝IP 10 QPS.处理完后随机休眠0.2~0.5秒
			try {
				Random random = new Random();
				int rand = (random.nextInt(4)+2)*100;
				Thread.sleep(rand);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
        
    }
    
   
}
