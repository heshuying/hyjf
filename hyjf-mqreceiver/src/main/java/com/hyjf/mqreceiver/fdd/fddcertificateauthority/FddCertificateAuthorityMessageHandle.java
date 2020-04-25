package com.hyjf.mqreceiver.fdd.fddcertificateauthority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hyjf.bank.service.fdd.fddcertificateauthority.FddCertificateAuthorityBean;
import com.hyjf.bank.service.fdd.fddcertificateauthority.FddCertificateAuthorityService;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;

/**
 * 法大大个人CA申请消息监听器
 *
 * @author liuyang
 */
@Component(value = "fddCertificateAuthorityMessageHandle")
public class FddCertificateAuthorityMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(FddCertificateAuthorityMessageHandle.class);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FddCertificateAuthorityService fddCertificateAuthorityService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // --> 消息检查
        if (message == null || message.getBody() == null) {
            _log.error("法大大CA认证接收消息为空");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息转换
        String msgBody = new String(message.getBody());

        _log.info("法大大CA认证接收消息：[" + msgBody + "].");


        FddCertificateAuthorityBean fddCertificateAuthorityBean;

        try {
            fddCertificateAuthorityBean = JSONObject.parseObject(msgBody, FddCertificateAuthorityBean.class);
            if (fddCertificateAuthorityBean == null || fddCertificateAuthorityBean.getUserId() == null || fddCertificateAuthorityBean.getUserId() == 0) {
                _log.info("解析为空：" + msgBody);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
            return;
        }

        _log.info("法大大CA认证逻辑处理开始,用户ID:[" + fddCertificateAuthorityBean.getUserId() + "].");

        try {
            Integer userId = fddCertificateAuthorityBean.getUserId();
            // 根据用户ID获取用户信息
            Users users = this.fddCertificateAuthorityService.getUsers(userId);
            if (users == null) {
                _log.info("根据用户ID获取用户信息失败,用户ID:[" + userId + "].");
                return;
            }
            UsersInfo usersInfo = this.fddCertificateAuthorityService.getUsersInfoByUserId(userId);
            if (usersInfo == null) {
                _log.info("根据用户ID获取用户详情信息失败,用户ID:[" + userId + "].");
                return;
            }
            // 调用法大大CA认证接口
            this.fddCertificateAuthorityService.updateUserCAInfo(userId);

            Map<String, String> map = Maps.newHashMap();
            map.put("userId", String.valueOf(userId));
            //crm出借推送
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,
                    RabbitMQConstants.ROUTINGKEY_BANCKOPEN_CRM, JSON.toJSONString(map));
            _log.info("开户发送MQ时间【{}】",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        } catch (Exception e) {
            _log.error("出错了",e);
            e.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
