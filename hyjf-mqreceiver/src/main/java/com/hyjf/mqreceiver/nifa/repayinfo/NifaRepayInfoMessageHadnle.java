/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.nifa.repayinfo;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.nifa.repayinfo.NifaRepayInfoService;
import com.hyjf.mqreceiver.user.openaccount.UserOpenAccountMessageHadnle;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author PC-LIUSHOUYI
 * @version NifaRepayInfoMessageHadnle, v0.1 2018/7/11 10:11
 */
public class NifaRepayInfoMessageHadnle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(NifaRepayInfoMessageHadnle.class);

    private String thisMessName = "【生成还款记录、合同状态、出借人回款信息】";

    @Autowired
    NifaRepayInfoService nifaRepayInfoService;

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
        Integer repayPeriod = jsonObject.getInteger("repayPeriod");
        if (StringUtils.isBlank(borrowNid) || null == repayPeriod) {
            _log.error(thisMessName + "通知参数不全.....");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理

        try {

            // 借款人项目还款记录数据生成
            boolean repayInfoResult = this.nifaRepayInfoService.insertNifaRepayInfo(borrowNid, repayPeriod);
            if (!repayInfoResult) {
                _log.error(thisMessName + "借款人项目还款记录数据生成失败！！borrowNid:" + borrowNid + " repayPeriod:" + repayPeriod);
            }

            // 合同状态变更数据生成
            boolean contractStatusResult = this.nifaRepayInfoService.insertNifaContractStatus(borrowNid,repayPeriod);
            if (!contractStatusResult) {
                _log.error(thisMessName + "合同状态变更数据生成失败！！borrowNid:" + borrowNid + " repayPeriod:" + repayPeriod);
            }

            // 出借人回款记录生成
            boolean receivedPaymentsResult = this.nifaRepayInfoService.insertNifaReceivedPayments(borrowNid, repayPeriod);
            if (!receivedPaymentsResult) {
                _log.error(thisMessName + "出借人回款记录生成失败！！borrowNid:" + borrowNid + " repayPeriod:" + repayPeriod);
            }

        } catch (Exception e) {
            e.printStackTrace();
            _log.error(thisMessName + "处理失败.....");
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info("----------------------" + thisMessName + "结束------------------------" + this.toString());
        }
    }
}
