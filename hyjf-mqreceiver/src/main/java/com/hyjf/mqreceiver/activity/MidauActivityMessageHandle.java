/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.activity;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.activity.MidauActivityService;
import com.hyjf.bank.service.activity.TwoElevenActvityService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.ActivityDateUtil;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;

/**
 * 活动
 * @author yinhui
 * @version MidauActivityMessageHandle, v0.1 2018/9/8 14:04
 */
public class MidauActivityMessageHandle implements ChannelAwareMessageListener {

    private Logger _log = LoggerFactory.getLogger(MidauActivityMessageHandle.class);

    @Autowired
    @Qualifier("bankMidauActivityServiceImpl")
    private MidauActivityService midauActivityService;

    @Autowired
    @Qualifier("bankTwoElevenActvityServiceImpl")
    private TwoElevenActvityService twoElevenActvityService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------双十一活动开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【双十一活动】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        String msgBody = new String(message.getBody());

        _log.info("【双十一活动】接收到的消息：" + msgBody);

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
            _log.error("【双十一活动】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        // 活动有效期校验
        String resultActivity = midauActivityService.checkActivityIfAvailable(ActivityDateUtil.TWOELEVEN_ACTIVITY_ID);

        //判断活动是否开始
        if (!"000".equals(resultActivity)) {
            _log.info("【双十一活动】 活动有效期校验不对 orderId: " + orderId + "resultActivity:"+resultActivity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return ;
        }


        String redisKey = RedisConstants.MIDAU_ACTIVITY + orderId;
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【双十一活动】正在处理....");
            return;
        }

        try {
            boolean flag = twoElevenActvityService.saveTwoelevenInvestment(userId,orderId,productType,investMoney);
            //防止加入汇计划 or 出借散标的时候 订单表orderId的还没有保存
            if(flag){
                Long code = RedisUtils.incr(RedisConstants.MIDAU_ACTIVITY_ORDERID_ + orderId);
                if(code == 5){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                    RedisUtils.del(RedisConstants.MIDAU_ACTIVITY_ORDERID_ + orderId);
                }else{
                    try {
                        Thread.sleep(1200);
                    }catch (Exception e){
                        _log.error("【双十一活动活动】线程睡眠失败，userId："  + userId + " orderId:" + orderId+" productType:"+productType, e);
                    }
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
                    RedisUtils.del(redisKey);
                }
                return;
            }
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【双十一活动】处理失败，userId："  + userId + " orderId:" + orderId+" productType:"+productType, e);
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        RedisUtils.del(redisKey);

        _log.info("----------------------------双十一活动结束 (orderId: " + orderId + ")--------------------------------" + this.toString());
    }
}
