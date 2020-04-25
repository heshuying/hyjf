/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.sensorsdata.hjh;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.sensorsdata.hjh.SensorsDataHjhInvestService;
import com.hyjf.bank.service.sensorsdata.login.SensorsDataLoginService;
import com.hyjf.mqreceiver.sensorsdata.login.SensorsDataLoginMessageHandle;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 神策数据采集:汇计划提交结果
 *
 * @author liuyang
 * @version SensorsDataHjhInvestMessageHandle, v0.1 2018/7/21 18:12
 */
public class SensorsDataHjhInvestMessageHandle implements ChannelAwareMessageListener {


    Logger _log = LoggerFactory.getLogger(SensorsDataHjhInvestMessageHandle.class);

    @Autowired
    private SensorsDataHjhInvestService sensorsDataHjhInvestService;


    /**
     * 消息监听
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("神策数据采集:汇计划相关");

        if (message == null || message.getBody() == null) {
            _log.error("神策数据采集:汇计划相关收到的消息为null");
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
                _log.info("神策数据采集:汇计划相关接收到的消息Bean为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 预置属性
            Map<String, Object> presetProps = sensorsDataBean.getPresetProps();
            // 匿名ID
            String distinctId = "";
            if (presetProps.get("_distinct_id") != null) {
                distinctId = (String) presetProps.get("_distinct_id");
            }
            //  设备ID
            String deviceId = "";
            if (presetProps.get("$device_id") != null) {
                deviceId = (String) presetProps.get("$device_id");
            }
            if (StringUtils.isBlank(distinctId) && StringUtils.isBlank(deviceId)  ) {
                _log.error("神策数据采集:汇计划相关,获取匿名ID为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 订单号
            String orderId = sensorsDataBean.getOrderId();
            if (StringUtils.isBlank(orderId)) {
                _log.error("神策数据采集:汇计划相关,获取加入订单号为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 事件类型
            String eventCode = sensorsDataBean.getEventCode();
            if (StringUtils.isBlank(eventCode)) {
                _log.error("神策数据采集:汇计划相关,获取事件类型为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            _log.info("神策数据采集:汇计划相关,出借订单号:[" + orderId + "].");
            // 发送神策数据
            this.sensorsDataHjhInvestService.sendSensorsData(sensorsDataBean);

        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
