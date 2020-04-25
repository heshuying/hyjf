/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.user.operation.log;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yaoyong
 * @version OperationLogHandle, v0.1 2018/10/8 16:13
 * 会员操作日志
 */
@Component(value = "operationLogHandle")
public class OperationLogHandle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(OperationLogHandle.class);
    private String thisName = "【会员操作日志】";

    @Autowired
    private UserOperationLogService userOperationLogService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.info("----------------------" + thisName + "开始------------------------" + this.toString());
        if (message == null || message.getBody() == null) {
            logger.error(thisName + "接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        String msgBody = new String(message.getBody());
        logger.info(thisName + "接收到的消息 ：" + msgBody);

        UserOperationLogEntity userOperationLogEntity;
        try {
            userOperationLogEntity = JSONObject.parseObject(msgBody, UserOperationLogEntity.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e1.printStackTrace();
            return;
        }

        //判断用户名是否存在
        if (Validator.isNull(userOperationLogEntity.getUserName())) {
            logger.error(thisName + "接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        //插入mongoDB
        try {
            userOperationLogService.insertOperationLog(userOperationLogEntity);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        }
    }
}
