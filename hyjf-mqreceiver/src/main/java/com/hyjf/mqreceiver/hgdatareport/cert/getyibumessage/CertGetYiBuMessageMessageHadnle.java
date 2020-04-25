/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.getyibumessage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mybatis.model.auto.CertLog;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 合规数据上报 CERT 查询批次数据入库消息 （延时队列）
 * @Author nxl
 * @Date 2018/12/25 17:57
 */
public class CertGetYiBuMessageMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertGetYiBuMessageMessageHadnle.class);

    private String thisMessName = "查询批次数据入库消息";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
    private static boolean isRun =false;
    @Autowired
    private CertGetYiBuMessageService certGetYiBuMessageService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.info(logHeader + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            logger.error(logHeader + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        if(isRun){
            logger.error(logHeader + "正在运行中！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        isRun = true ;
        String msgBody = new String(message.getBody());
        logger.info(logHeader + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.error(logHeader + "解析消息体失败！！！", e);
            isRun = false;
            return;
        }

        /*String batchNum = jsonObject.getString("batchNum");*/
        String mqMsgId = jsonObject.getString("mqMsgId");
        if (StringUtils.isBlank(mqMsgId)) {
            logger.error(logHeader + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            isRun = false;
            return;
        }

        // 检查redis的值是否允许运行 允许返回true  不允许返回false
        boolean canRun = certGetYiBuMessageService.checkCanRun();
        if(!canRun){
            logger.info(logHeader + "redis不允许上报！");
            isRun = false;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            // --> 调用service组装数据
            List<CertLog> certLogList = certGetYiBuMessageService.getCertLog();
            if(null!=certLogList&&certLogList.size()>0){
                for(CertLog certLog:certLogList){
                    int start = certLog.getLogOrdId().indexOf("_");
                    String bachNum = certLog.getLogOrdId().substring(start+1);
                    String infType  = certLog.getLogOrdId().substring(0,start);
                    CertReportEntity certReportEntity = certGetYiBuMessageService.updateYiBuMessage(bachNum,certLog.getId().toString(),infType);
                    logger.info(logHeader + "返回结果为:"+JSONObject.toJSON(certReportEntity));
                }
                logger.info(logHeader + " 处理成功。" + msgBody);
            }
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            isRun = false ;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。");
        }
    }
}
