/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.accountlist;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.CertAccountListDao;
import com.hyjf.mongo.hgdatareport.entity.CertAccountList;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.transact.OldCertTransactService;
import com.rabbitmq.client.Channel;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 合规数据上报 CERT 历史数据上报交易明细信息推送上报（延时队列）
 * @Author pcc
 * @Date 2018/11/26 17:57
 */
public class  OldCertAccountlListMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(OldCertAccountlListMessageHadnle.class);

    private String thisMessName = "交易明细信息历史数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";


    @Autowired
    private OldCertTransactService oldCertTransactService;
    @Autowired
    private CertAccountListDao certAccountListDao;
    static boolean isRun = false;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
    	Integer now=GetDate.getMyTimeInMillis();
    	logger.info(logHeader + " 开始。"+now);
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            logger.error(logHeader + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        if(isRun){
            logger.error(logHeader + "正在运行！");
            isRun = false;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        isRun = true;
        String msgBody = new String(message.getBody());
        logger.info(logHeader + "接收到的消息：" + msgBody);
        // --> 消息处理
        try {
            // 查询未上报的交易明细
        	List<CertAccountList> list = certAccountListDao.getNotSendAccountList();
        	logger.info(logHeader+"待上报的批次数量:"+(list==null?"0":list.size()));
            if (list == null || list.size() == 0) {
                logger.info(logHeader + "全部执行完成");
                isRun = false;
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return ;
            }

            for (CertAccountList accountList : list) {
                CertReportEntity entity = new CertReportEntity();
                PropertyUtils.copyProperties(entity, accountList);
                // 上送数据
                try {
                    entity = oldCertTransactService.insertAndSendPost(entity);
                } catch (Exception e) {
                    throw e;
                }
                // 上报完毕 修改状态为成功
                certAccountListDao.updateAccountSuccess(accountList);
            }
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。"+(GetDate.getMyTimeInMillis()-now));
            isRun = false;
        }
       
    }
}
