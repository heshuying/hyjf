/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.creditor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.creditor.CertTenderInfoService;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser.CertOldBorrowUserService;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.repayplan.CertOldRepayPlanBean;
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
import java.util.List;

/**
 * @Description 合规数据上报 CERT 债权信息信息信息上报
 * @Author nxl
 * @Date 2018/11/28 10:57
 */
public class CertOldTenderInfoMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertOldTenderInfoMessageHadnle.class);

    private String thisMessName = "债权信息历史数据推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    private CertOldTenderInfoService certOldTenderInfoService;
    @Autowired
    private CertTenderInfoService certTenderInfoService;
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
        // --> 调用service查询数据
        List<CertBorrow> certBorrowEntityList = certOldTenderInfoService.insertCertBorrowCreditList();
        try {
            int intCount = certBorrowEntityList == null ? 0 : certBorrowEntityList.size();
            logger.info(logHeader + "查询的债权信息数据共: " + intCount + "条" + ",当前时间为:" + GetDate.getNowTime10());
            if (null != certBorrowEntityList && certBorrowEntityList.size() > 0) {
                //修改状态(首先将状态更新为成功
                updateAllIsCreditStatus(certBorrowEntityList, true);
                // 修改状态  end
                JSONArray listRepay = new JSONArray();
                for (CertBorrow certBorrowEntity : certBorrowEntityList) {
                    String borrowNid = certBorrowEntity.getBorrowNid();
                    listRepay = certTenderInfoService.getBorrowTender(borrowNid, listRepay, true);
                }
                logger.info(logHeader + "债权信息数据组装完毕,数据共" + listRepay.size() + "条" + ",当前时间为:" + GetDate.getNowTime10());
                //转换为list
                List<JSONArray> jsonArrayList = new ArrayList<JSONArray>();
                List<CertOldTenderInfoBean> certOldRepayPlanBeans = JSONArray.parseArray(listRepay.toJSONString(), CertOldTenderInfoBean.class);
                if (null != certOldRepayPlanBeans) {
                    //拆分数据,防止数据长多过长
                    List<List<CertOldTenderInfoBean>> parts = Lists.partition(certOldRepayPlanBeans, 3000);
                    for (List<CertOldTenderInfoBean> child : parts) {
                        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(child));
                        jsonArrayList.add(jsonArray);
                    }
                }
                logger.info(logHeader + "债权信息数据拆分完毕,共" + jsonArrayList.size() + "条" + ",当前时间为:" + GetDate.getNowTime10());
                if (null != jsonArrayList && jsonArrayList.size() > 0) {
                    for (int i = 0; i < jsonArrayList.size(); i++) {
                        JSONArray repay = jsonArrayList.get(i);
                        // 上送数据
                        List<CertReportEntity> entitys = CertCallUtil.groupByDate(repay, thisMessName, CertCallConstant.CERT_INF_TYPE_CREDITOR);
                        // 遍历循环上报
                        for (CertReportEntity entity : entitys) {
                            try {
                                certTenderInfoService.insertAndSendPost(entity);
                            } catch (Exception e) {
                                throw e;
                            }
                            logger.info(logHeader + "上报返回状态为:" + entity.getReportStatus());
                        }
                    }
                }
            }
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            //如果发生错误,将状态修改为99失败
            updateAllIsCreditStatus(certBorrowEntityList, false);
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。");
        }
    }

    private void updateAllIsCreditStatus(List<CertBorrow> certBorrowEntityList, boolean isSuccess) {
        // 更新 isCredit 将是否上送债权信息 赋值1
        // 批量修改状态  start
        List<Integer> ids = new ArrayList<>();
        for (CertBorrow item : certBorrowEntityList) {
            ids.add(item.getId());
        }
        if (ids.size() > 0) {
            CertBorrowUpdate update = new CertBorrowUpdate();
            update.setIds(ids);
            CertBorrow certBorrow = new CertBorrow();
            if (isSuccess) {
                // 成功
                certBorrow.setIsCredit(1);
            } else {
                // 失败
                certBorrow.setIsCredit(99);
            }
            update.setCertBorrow(certBorrow);
            // 批量修改
            certOldBorrowUserService.updateCertBorrowStatusBatch(update);
        }
        // 批量修改状态  end
    }
}
