package com.hyjf.mqreceiver.fdd.fdduserinfochange;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fdduserinfochange.FddUserInfoBean;
import com.hyjf.bank.service.fdd.fdduserinfochange.FddUserInfoChangeService;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 法大大客户信息修改消息监听器
 *
 * @author liuyang
 */
@Component(value = "fddUserInfoChangeMessageHandle")
public class FddUserInfoChangeMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(FddUserInfoChangeMessageHandle.class);

    @Autowired
    private FddUserInfoChangeService fddUserInfoChangeService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // --> 消息检查
        if (message == null || message.getBody() == null) {
            _log.error("法大大客户信息修改接收消息为空");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息转换
        String msgBody = new String(message.getBody());

        _log.info("法大大客户信息修改接收消息：[" + msgBody + "].");


        FddUserInfoBean fddUserInfoBean;
        try {
            fddUserInfoBean = JSONObject.parseObject(msgBody, FddUserInfoBean.class);
            if (fddUserInfoBean == null || fddUserInfoBean.getUserId() == null || fddUserInfoBean.getUserId() == 0) {
                _log.info("解析为空：" + msgBody);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e1.printStackTrace();
            return;
        }
        _log.info("法大大客户信息修改逻辑处理开始,用户ID:[" + fddUserInfoBean.getUserId() + "].");
        try {
            // 根据用户ID获取用户信息
            Users users = this.fddUserInfoChangeService.getUsers(fddUserInfoBean.getUserId());
            if (users == null) {
                _log.info("根据用户ID查询用户信息失败,用户不存在,用户ID:[" + fddUserInfoBean.getUserId() + "].");
                return;
            }
            UsersInfo usersInfo = this.fddUserInfoChangeService.getUsersInfoByUserId(fddUserInfoBean.getUserId());
            if (usersInfo == null) {
                _log.info("根据用户ID查询用户详情信息失败,用户ID:[" + fddUserInfoBean.getUserId() + "].");
                return;
            }
            // 根据用户ID查询法大大CA认证相关信息
            CertificateAuthority certificateAuthority = this.fddUserInfoChangeService.selectCAInfoByUserId(fddUserInfoBean.getUserId());
            if (certificateAuthority == null) {
                _log.info("根据用户ID获取用户CA认证信息失败,用户ID:[" + fddUserInfoBean.getUserId() + "]");
                return;
            }
            // 获取手机号
            String mobile = certificateAuthority.getMobile();
            // 获取用户当前手机号
            String nowMobile = users.getMobile();
            // 如果手机号相等,不需要重新请求法大大
            if (mobile.equals(nowMobile)) {
                _log.info("手机号相等,不需要重新请求法大大,用户ID:[" + fddUserInfoBean.getUserId() + "],CA认证时手机号:[" + mobile + "],最新手机号:[" + nowMobile + "].");
                return;
            }
            // 调用大大客户信息修改接口,修改用户CA认证相关信息
            this.fddUserInfoChangeService.updateUserCAInfo(fddUserInfoBean.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
