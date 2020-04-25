/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.sensorsdata.recharge;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.sensorsdata.recharge.SensorsDataRechargeService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 神策数据采集:充值相关
 *
 * @author liuyang
 * @version SensorsDataRechargeMessageHandle, v0.1 2018/7/11 16:57
 */
public class SensorsDataRechargeMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(SensorsDataRechargeMessageHandle.class);

    @Autowired
    private SensorsDataRechargeService sensorsDataRechargeService;

    /**
     * 消息监听
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("神策数据采集:充值相关");

        if (message == null || message.getBody() == null) {
            _log.error("神策数据采集:充值相关接收到的消息为null");
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
                _log.info("神策数据采集:充值相关接收到的消息Bean为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 充值订单号
            String orderId = sensorsDataBean.getOrderId();
            if (StringUtils.isBlank(orderId)) {
                _log.info("神策数据采集:充值订单号为空");
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

            // 发送神策数据
            this.sensorsDataRechargeService.sendSensorsData(sensorsDataBean);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
