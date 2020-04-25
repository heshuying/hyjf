/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.sensorsdata.openaccount;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.sensorsdata.openaccount.SensorsDataOpenAccountService;
import com.hyjf.common.cache.RedisUtils;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 神策数据统计:用户开户结果
 *
 * @author liuyang
 * @version SensorsDataOpenAccountMessageHandle, v0.1 2018/9/27 9:06
 */
public class SensorsDataOpenAccountMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(SensorsDataOpenAccountMessageHandle.class);

    @Autowired
    private SensorsDataOpenAccountService sensorsDataOpenAccountService;

    /**
     * 消息监听
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("神策数据采集:开户相关");

        if (message == null || message.getBody() == null) {
            _log.error("神策数据采集:开户相关接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        try {
            String msgBody = new String(message.getBody());
            SensorsDataBean sensorsDataBean;
            try {
                sensorsDataBean = JSONObject.parseObject(msgBody, SensorsDataBean.class);
            } catch (Exception e1) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                e1.printStackTrace();
                return;
            }

            if (sensorsDataBean == null) {
                _log.info("神策数据采集:开户相关接收到的消息Bean为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 用户ID
            Integer userId = sensorsDataBean.getUserId();
            if (userId == null || userId == 0) {
                _log.info("神策数据采集:开户用户ID为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            // 事件类型
            String evenCode = sensorsDataBean.getEventCode();
            if (StringUtils.isBlank(evenCode)) {
                _log.info("神策数据采集:事件类型为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 从redis里取看redis里是否存在
            if (!RedisUtils.exists("SENSORS_DATA_OPEN_ACCOUNT:" + userId)) {
                // 如果不存在
                _log.info("神策数据采集:用户ID在redis里不存在");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 发送神策数据
            this.sensorsDataOpenAccountService.sendSensorsData(sensorsDataBean);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }


    }
}
