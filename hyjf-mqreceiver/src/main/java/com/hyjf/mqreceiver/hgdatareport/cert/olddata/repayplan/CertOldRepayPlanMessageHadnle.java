/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.repayplan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser.CertOldBorrowUserService;
import com.hyjf.mqreceiver.hgdatareport.cert.repayplan.CertRepayPlanService;
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
 * @Description 合规数据上报 CERT 还款计划信息历史数据上报
 * @Author nxl
 * @Date 2018/11/28 10:57
 */
public class CertOldRepayPlanMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertOldRepayPlanMessageHadnle.class);

    private String thisMessName = "还款计划信息历史数据推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    private CertOldRepayPlanService certOldRepayPlanService;
    @Autowired
    private CertRepayPlanService certRepayPlanService;
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

        JSONObject jsonObject;
        // --> 消息处理
        try {
            List<CertBorrow> certBorrowEntityList = certOldRepayPlanService.insertCertBorrowRapayList();
            int intCount = certBorrowEntityList == null ? 0 : certBorrowEntityList.size();
            logger.info(logHeader + "查询的还款计划数据共: " + intCount + "条" + ",当前时间为:" + GetDate.getNowTime10());
            if (null != certBorrowEntityList && certBorrowEntityList.size() > 0) {
                JSONArray listRepay = new JSONArray();
                for (CertBorrow certBorrowEntity : certBorrowEntityList) {
                    String borrowNid = certBorrowEntity.getBorrowNid();
                    // 上送数据
                    listRepay = certRepayPlanService.getBorrowReyapPlan(borrowNid, listRepay, true);
                }

                logger.info(logHeader + "还款计划数据组装完毕,数据共" + listRepay.size() + "条" + ",当前时间为:" + GetDate.getNowTime10());

                List<JSONArray> jsonArrayList = new ArrayList<JSONArray>();
                //转换为list
                List<CertOldRepayPlanBean> certOldRepayPlanBeans = JSONArray.parseArray(listRepay.toJSONString(), CertOldRepayPlanBean.class);
                if(null!=certOldRepayPlanBeans){
                    //拆分数据,防止数据长多过长
                    List<List<CertOldRepayPlanBean>> parts = Lists.partition(certOldRepayPlanBeans, 3000);
                    for(List<CertOldRepayPlanBean> child:parts){
                        JSONArray jsonArray =JSONArray.parseArray(JSON.toJSONString(child));
                        jsonArrayList.add(jsonArray);
                    }
                }
                logger.info(logHeader + "还款计划数据拆分完毕,共" + jsonArrayList.size() + "条" + ",当前时间为:" + GetDate.getNowTime10());
                if(null!=jsonArrayList&&jsonArrayList.size()>0){
                    for(int i=0;i<jsonArrayList.size();i++){
                        JSONArray repay = jsonArrayList.get(i);
                        List<CertReportEntity> entitys = CertCallUtil.groupByDate(repay, thisMessName, CertCallConstant.CERT_INF_TYPE_REPAY_PLAN);
                        // 遍历循环上报
                        for (CertReportEntity entity : entitys) {
                            try {
                                certRepayPlanService.insertAndSendPost(entity);
                            } catch (Exception e) {
                                throw e;
                            }
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
                                    certBorrow.setIsRepayPlan(1);
                                } else {
                                    // 失败
                                    certBorrow.setIsRepayPlan(99);
                                }
                                update.setCertBorrow(certBorrow);
                                // 批量修改
                                certOldBorrowUserService.updateCertBorrowStatusBatch(update);
                            }
                            // 批量修改状态  end
                        }
                    }
                }
                logger.info(logHeader + "还款计划数据拆分完毕,数据共" + listRepay.size() + "条" + ",当前时间为:" + GetDate.getNowTime10());
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
