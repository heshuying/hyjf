package com.hyjf.mqreceiver.crm.account;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component(value = "newcrmAccountMessageHandle")
public class NewCrmAccountMessageHandle extends BaseCrmAccountMessageHandle{

    /**
     * 消息监听
     * @throws Exception
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        super.onMessage(message,channel);
    }
}
