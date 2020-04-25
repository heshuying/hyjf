/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.activity;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.activity.SendCouponActivityService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 发放活动优惠券
 * @author yinhui
 * @version SendCouponActivityMessageHandle, v0.1 2018/10/16 15:45
 */
public class SendCouponActivityMessageHandle implements ChannelAwareMessageListener {

    private Logger _log = LoggerFactory.getLogger(SendCouponActivityMessageHandle.class);

    @Autowired
    private SendCouponActivityService sendCouponActivityService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------双十一发放优惠券开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【双十一发放优惠券】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        String msgBody = new String(message.getBody());

        _log.info("【双十一发放优惠券】接收到的消息：" + msgBody);

        JSONObject requestJson;
        //用户ID
        Integer userId = null;
        //优惠券编号
        String couponCode = null;
        //发放奖励表ID
        Integer rewardId = null;
        try {
            requestJson = JSONObject.parseObject(msgBody);

            userId = (Integer) requestJson.get("userId");
            couponCode = (String)requestJson.get("couponCode");
            rewardId = (Integer) requestJson.get("rewardId");

        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }

        //验证请求参数
        if (Validator.isNull(requestJson.get("userId")) || Validator.isNull(requestJson.get("couponCode"))
                || Validator.isNull(requestJson.get("rewardId"))) {
            _log.error("【双十一发放优惠券】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        String redisKey = RedisConstants.SEND_COUPON_ACTIVITY_ + rewardId;
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【双十一发放优惠券】正在处理....");
            return;
        }

        try {
            sendCouponActivityService.sendCoupon(userId,couponCode,rewardId);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("【双十一发放优惠券】处理失败，userId："  + userId + " rewardId:" + rewardId+" couponCode:"+couponCode, e);
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        RedisUtils.del(redisKey);

        _log.info("----------------------------双十一发放优惠券结束 (orderId: " + rewardId + ")--------------------------------" + this.toString());
    }
}
