/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.transferproject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.rabbitmq.client.Channel;

/**
 * @Description 合规数据上报 CERT 转让项目信息推送上报（延时队列）
 * @Author pcc
 * @Date 2018/11/26 17:57
 */
public class CertTransferProjectMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertTransferProjectMessageHadnle.class);

    private String thisMessName = "转让项目信息";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";


    @Autowired
    private CertTransferProjectService certTransferProjectService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.info(logHeader + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            logger.error(logHeader + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        logger.info(logHeader + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.error(logHeader + "解析消息体失败！！！", e);
            return;
        }

        String creditNid = jsonObject.getString("creditNid");
        String flag = jsonObject.getString("flag");
        String tradeDate = jsonObject.getString("tradeDate");

        if (StringUtils.isBlank(creditNid)) {
            logger.error(logHeader + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // 检查redis的值是否允许运行 允许返回true  不允许返回false
        boolean canRun = certTransferProjectService.checkCanRun();
        if(!canRun){
            logger.info(logHeader + "redis不允许上报！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            // --> 增加防重校验（根据不同平台不同上送方式校验不同）

            // --> 调用service组装数据
            JSONArray data =certTransferProjectService.createDate(creditNid,flag);
            // 上送数据
            CertReportEntity entity = new CertReportEntity(thisMessName, CertCallConstant.CERT_INF_TYPE_TRANSFER_PROJECT, creditNid, data);
            try {
                // 掉单用
                if(tradeDate!=null&&!"".equals(tradeDate)){
                    entity.setTradeDate(tradeDate);
                }
                entity = certTransferProjectService.insertAndSendPost(entity);
            } catch (Exception e) {
                throw e;
            }
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。");
        }
    }
}
