/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.status;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser.CertOldBorrowUserService;
import com.hyjf.mqreceiver.hgdatareport.cert.status.CertBorrowStatusService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 散标状态上报(旧数据上报)
 * @Author nxl
 * @Date 2018/11/28 10:57
 */
public class CertOldBorrowStatusMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertOldBorrowStatusMessageHadnle.class);

    private String thisMessName = "数据散标状态信息历史数据推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
    @Autowired
    private CertOldBorrowStatusService certOldBorrowStatusService;
    @Autowired
    private CertBorrowStatusService certBorrowStatusService;
    @Autowired
    private CertOldBorrowUserService certOldBorrowUserService;

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
        // --> 消息处理
        try {
            //查询
            List<CertBorrow> certBorrowEntityList = certOldBorrowStatusService.insertCertBorrowStatusList();
            int intCount = certBorrowEntityList == null ? 0 : certBorrowEntityList.size();
            logger.info(logHeader + "查询的散标状态数据共: " + intCount + "条" + ",当前时间为:" + GetDate.getNowTime10());
            if (null != certBorrowEntityList && certBorrowEntityList.size() > 0) {
                JSONArray listRepay = new JSONArray();
                Map<String, Object> param = new HashMap<String, Object>();
                for (CertBorrow certBorrowEntity : certBorrowEntityList) {
                    //获取borrowNid
                    String borrowNid = certBorrowEntity.getBorrowNid();
                    // --> 调用service组装数据
                    param = certBorrowStatusService.selectBorrowByBorrowNid(borrowNid, null,false,true);
                    if(null!=param){
                        listRepay.add(param);
                    }
                    //
                }
                logger.info(logHeader + "散标状态数据组装完毕,数据共" + listRepay.size() + "条" + ",当前时间为:" + GetDate.getNowTime10());
                //调用service组装数据
//                CertReportEntity entity = new CertReportEntity(thisMessName, CertCallConstant.CERT_INF_TYPE_STATUS, null, listRepay);
                List<CertReportEntity> entitys = CertCallUtil.groupByDate(listRepay,thisMessName,CertCallConstant.CERT_INF_TYPE_STATUS);
                // 遍历循环上报
                for (CertReportEntity entity:entitys) {
                    try {
                        entity = certBorrowStatusService.insertAndSendPost(entity);
                        logger.info(logHeader + "上报完成,数量为:" + entity.getDataList().size() + "，结果为:" + entity.getRetMess());
                        // 批量修改状态  start
                        List<Integer> ids = new ArrayList<>();
                        for (CertBorrow item : certBorrowEntityList) {
                            ids.add(item.getId());
                        }
                        if (ids.size() > 0) {
                            CertBorrowUpdate update = new CertBorrowUpdate();
                            update.setIds(ids);
                            CertBorrow certBorrow = new CertBorrow();
                            if (entity != null && CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(entity.getReportStatus())) {
                                // 成功
                                certBorrow.setIsStatus(1);
                            } else {
                                // 失败
                                certBorrow.setIsStatus(99);
                            }
                            update.setCertBorrow(certBorrow);
                            // 批量修改
                            certOldBorrowUserService.updateCertBorrowStatusBatch(update);
                        }
                        // 批量修改状态  end
                    } catch (Exception e) {
                        throw e;
                    }
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
