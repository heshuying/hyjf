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
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;

/**
 * 纳觅返现活动
 * @author tanyy
 * @version ReturnCashActivityMessageHandle, v0.1 2018/11/8 14:04
 */
public class ReturnCashOneActivityMessageHandle implements ChannelAwareMessageListener {

    private Logger _log = LoggerFactory.getLogger(ReturnCashOneActivityMessageHandle.class);

    @Autowired
    @Qualifier("bankMidauActivityServiceImpl")
    private MidauActivityService midauActivityService;

    @Autowired
    private ReturnCashActivityService returnCashActivityService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------散标放款时间处理------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【散标放款时间处理】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        String msgBody = new String(message.getBody());

        _log.info("【散标放款时间处理】接收到的消息：" + msgBody);

        JSONObject requestJson;
        String borrowNid = null;
        int nowTime = 0;
        try {
            requestJson = JSONObject.parseObject(msgBody);

            borrowNid = (String) requestJson.get("borrowNid");
            nowTime = (int)requestJson.get("nowTime");
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }

        //验证请求参数
        if (Validator.isNull(requestJson.get("borrowNid")) || Validator.isNull(requestJson.get("nowTime"))) {
            _log.error("【散标放款时间处理】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        try {
            returnCashActivityService.updateJoinTime(borrowNid,nowTime);
        }catch (Exception e){
            _log.error("【散标放款时间】处理失败", e);
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        _log.info("----------------------------散标放款时间处理结束--------------------------------" + this.toString());
    }
}
