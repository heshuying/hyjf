/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.nifa.contractessence;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.nifa.contractessence.NifaContractEssenceService;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaContractEssenceMessageHadnle, v0.1 2018/7/9 11:16
 */
public class NifaContractEssenceMessageHadnle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(NifaContractEssenceMessageHadnle.class);

    private String thisMessName = "【生成合同要素信息】";
//    private String

    @Autowired
    NifaContractEssenceService nifaContractEssenceService;

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
        if (StringUtils.isBlank(borrowNid)) {
            _log.error(thisMessName + "通知参数不全.....");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理

        try {

            _log.info(thisMessName + " 开始处理，借款编号:" + borrowNid);

            // 解决主从不同步问题、主库查询
            List<BorrowTender> borrowTenderList = this.nifaContractEssenceService.tenderListByBorrowNid(borrowNid);

            if (null != borrowTenderList && borrowTenderList.size() > 0) {
                for (BorrowTender borrowTender : borrowTenderList) {
                    this.nifaContractEssenceService.insertContractEssence(borrowNid, borrowTender.getNid(), borrowTender);
                }
            } else {
                _log.error(thisMessName + "未查询到出借信息：借款编号=" + borrowNid);
            }

            _log.info(thisMessName + " 结束处理，借款编号:" + borrowNid);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
