package com.hyjf.mqreceiver.crm.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.mqreceiver.crm.utils.PropUtils;
import com.rabbitmq.client.Channel;

@Component(value = "crmAccountMessageHandle")
public class CrmAccountMessageHandle extends BaseCrmAccountMessageHandle {

    @Autowired
    BorrowService borrowService;

    private String CRM_INSERTCUSTOMER_ACTION_URL = PropUtils.getSystem("crm.updateCustomer.url");

    /**
     * 消息监听
     * @throws Exception
     */
    public void onMessage(Message message, Channel channel) throws Exception {
       super.onMessage(message,channel);
    }

}
