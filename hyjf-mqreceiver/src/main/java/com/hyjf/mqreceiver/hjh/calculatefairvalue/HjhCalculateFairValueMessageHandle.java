package com.hyjf.mqreceiver.hjh.calculatefairvalue;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.hjh.borrow.calculatefairvalue.HjhCalculateFairValueBean;
import com.hyjf.bank.service.hjh.borrow.calculatefairvalue.HjhCalculateFairValueService;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 每一小时计算加入订单的公允价值
 *
 * @author liuyang
 */
public class HjhCalculateFairValueMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(HjhCalculateFairValueMessageHandle.class);

    @Autowired
    private HjhCalculateFairValueService hjhCalculateFairValueService;

    /**
     * 消息监听
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("计划加入订单计算公允价值开始");

        if (message == null || message.getBody() == null) {
            _log.error("计划加入订单计算公允价值接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        try {
            String msgBody = new String(message.getBody());
            HjhCalculateFairValueBean hjhCalculateFairValueBean;
            try {
                hjhCalculateFairValueBean = JSONObject.parseObject(msgBody, HjhCalculateFairValueBean.class);
            } catch (Exception e1) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                e1.printStackTrace();
                return;
            }
            // 计划加入订单号
            String accedeOrderId = hjhCalculateFairValueBean.getAccedeOrderId();
            // 计划订单号为空
            if (StringUtils.isBlank(accedeOrderId)) {
                _log.error("计划订单号为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 计算类型
            Integer calculateType = hjhCalculateFairValueBean.getCalculateType();
            // 根据加入订单号查询计划订单
            HjhAccede hjhAccede = this.hjhCalculateFairValueService.selectHjhAccedeByAccedeOrderId(accedeOrderId);

            if (hjhAccede == null) {
                _log.error("根据计划加入订单号查询计划订单为空,计划加入订单号:[" + accedeOrderId + "].");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            // 计划加入订单状态为:已退出
            if (hjhAccede.getOrderStatus() == 7) {
                _log.info("计划订单已退出,不需要计算公允价值,计划加入订单号:[" + accedeOrderId + "].");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            _log.info("计划加入订单计算公允价值:计划订单号:[" + accedeOrderId + "].");
            // 计算加入订单的公允价值
            this.hjhCalculateFairValueService.calculateFairValue(hjhAccede, calculateType);

        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
