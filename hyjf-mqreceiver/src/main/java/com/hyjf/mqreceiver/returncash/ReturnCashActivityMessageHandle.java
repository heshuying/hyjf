/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.returncash;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.activity.MidauActivityService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.ActivityDateUtil;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 纳觅返现活动
 * @author tanyy
 * @version ReturnCashActivityMessageHandle, v0.1 2018/11/8 14:04
 */
public class ReturnCashActivityMessageHandle implements ChannelAwareMessageListener {

    private Logger _log = LoggerFactory.getLogger(ReturnCashActivityMessageHandle.class);

    @Autowired
    @Qualifier("bankMidauActivityServiceImpl")
    private MidauActivityService midauActivityService;

    @Autowired
    private ReturnCashActivityService returnCashActivityService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------纳觅返现活动------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【纳觅返现活动】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        String msgBody = new String(message.getBody());

        _log.info("【纳觅返现活动】接收到的消息：" + msgBody);

        JSONObject requestJson;
        Integer userId = null;
        String orderId = null;
        Integer productType = null;
        BigDecimal investMoney = BigDecimal.ZERO;
        try {
            requestJson = JSONObject.parseObject(msgBody);

            userId = (Integer) requestJson.get("userId");
            orderId = (String)requestJson.get("orderId");
            productType = (Integer) requestJson.get("productType");
            investMoney = new BigDecimal((String) requestJson.get("investMoney"));

        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }

        //验证请求参数
        if (Validator.isNull(requestJson.get("userId")) || Validator.isNull(requestJson.get("orderId"))
                || Validator.isNull(requestJson.get("productType"))
                || Validator.isNull(requestJson.get("investMoney"))) {
            _log.error("【纳觅返现活动】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        // 活动有效期校验
        String resultActivity = midauActivityService.checkActivityIfAvailable(ActivityDateUtil.RETURNCASH_ACTIVITY_ID);

        //判断活动是否开始
        if (!"000".equals(resultActivity)) {
            _log.info("【纳觅返现活动】 活动有效期校验不对 orderId: " + orderId + "resultActivity:"+resultActivity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return ;
        }


        String redisKey = RedisConstants.RETURN_CASH_ACTIVITY + orderId;
        boolean result = RedisUtils.tranactionSet(redisKey, 60);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【纳觅返现活动】正在处理....");
            return;
        }

        try {
             returnCashActivityService.saveReturnCash(userId,orderId,productType,investMoney);
        } catch (Exception e) {
            _log.error("【纳觅返现活动】处理失败，userId："  + userId + " orderId:" + orderId+" productType:"+productType, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            return;
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        RedisUtils.del(redisKey);
        _log.info("----------------------------纳觅返现活动结束 (orderId: " + orderId + ")--------------------------------" + this.toString());
    }
}
