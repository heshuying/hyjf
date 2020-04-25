/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.nifa.repaylate;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.nifa.repaylate.NifaRepayLateService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author PC-LIUSHOUYI
 * @version NifaRepayLateMessageHadnle, v0.1 2018/7/14 10:02
 */
public class NifaRepayLateMessageHadnle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(NifaRepayLateMessageHadnle.class);

    private String thisMessName = "【更新合同信息逾期/完全债转】";

    @Autowired
    NifaRepayLateService nifaRepayLateService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------" + thisMessName + "开始------------------------" + this.toString());
        if (message == null || message.getBody() == null) {
            _log.error(thisMessName + "接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        _log.info(thisMessName + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.error("解析消息体失败...", e);
            return;
        }

        String borrowNid = jsonObject.getString("borrowNid");
        String nid = jsonObject.getString("nid");
        String contractStatus = jsonObject.getString("contractStatus");
        if ((StringUtils.isBlank(borrowNid) && StringUtils.isBlank(nid)) || StringUtils.isBlank(contractStatus)) {
            _log.error(thisMessName + "通知参数不全.....");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理

        try {
            if ("2".equals(contractStatus)){
                boolean result = this.nifaRepayLateService.insertNifaRepayLateInfo(borrowNid);
                if (!result) {
                    _log.error(thisMessName + "逾期还款信息更新失败.....");
                    return;
                }
            } else if ("6".equals(contractStatus)){
                boolean result = this.nifaRepayLateService.insertNifaRepayCreaditInfo(nid);
                if (!result) {
                    _log.error(thisMessName + "完全债转还款信息更新失败.....");
                    return;
                }
            }
        } catch (Exception e) {
            _log.error(thisMessName + "还款信息更新异常.....");
        }finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
