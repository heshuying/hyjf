/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.status;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.dao.CertReportDao;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.cert.open.CertToolV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description 合规数据上报 CERT 散标状态上报
 * @Author nxl
 * @Date 2018/11/28 10:57
 */
public class CertBorrowStatusMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertBorrowStatusMessageHadnle.class);

    private String thisMessName = "散标状态信息推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
    public static CertToolV1 tool = new CertToolV1();

    @Autowired
    private CertBorrowStatusService certBorrowStatusService;

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

        String borrowNid = jsonObject.getString("borrowNid");
        String tradeDate = jsonObject.getString("tradeDate");

        if (StringUtils.isBlank(borrowNid)) {
            logger.error(logHeader + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // 检查redis的值是否允许运行 允许返回true  不允许返回false
        boolean canRun = certBorrowStatusService.checkCanRun();
        if(!canRun){
            logger.info(logHeader + "redis不允许上报！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            // --> 调用service组装数据
            JSONArray listRepay  = new JSONArray();
            Map<String,Object> mapParam = certBorrowStatusService.selectBorrowByBorrowNid(borrowNid,null,false,false);
            listRepay.add(mapParam);
            logger.info("数据："+listRepay);
            // 上送数据
            CertReportEntity entity = new CertReportEntity(thisMessName, CertCallConstant.CERT_INF_TYPE_STATUS, borrowNid, listRepay);
            try {
                // 掉单用
                if(tradeDate!=null&&!"".equals(tradeDate)){
                    entity.setTradeDate(tradeDate);
                }
                certBorrowStatusService.insertAndSendPost(entity);
            } catch (Exception e) {
                throw e;
            }
            //判断标的状态 = 9 的,再报送一次 添加判断=9
            // 上送数据
            String productStatus = mapParam.get("productStatus").toString();
            if(productStatus.equals("9")){
                // --> 调用service组装数据
                JSONArray listRepayAfter  = new JSONArray();
                Map<String,Object> mapParamAfter = certBorrowStatusService.selectBorrowByBorrowNid(borrowNid,productStatus,false,false);
                listRepayAfter.add(mapParamAfter);
                logger.info("数据："+listRepayAfter);
                // 上送数据
                CertReportEntity entityAfter = new CertReportEntity(thisMessName, CertCallConstant.CERT_INF_TYPE_STATUS, borrowNid, listRepayAfter);
                try {
                    // 掉单用
                    if(tradeDate!=null&&!"".equals(tradeDate)){
                        entityAfter.setTradeDate(tradeDate);
                    }
                    certBorrowStatusService.insertAndSendPost(entityAfter);
                } catch (Exception e) {
                    throw e;
                }
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
