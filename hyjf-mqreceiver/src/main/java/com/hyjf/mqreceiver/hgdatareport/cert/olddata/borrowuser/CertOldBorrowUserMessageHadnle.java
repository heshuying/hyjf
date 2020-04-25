/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.customize.CertSendUser;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心 借款人老数据上报
 * @Author sunss
 * @Date 2018/11/26 17:57
 */
public class CertOldBorrowUserMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertOldBorrowUserMessageHadnle.class);

    private String thisMessName = "借款人老数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    // 是否正在运行
    private static boolean isRun = false;

    @Autowired
    private CertOldBorrowUserService certOldBorrowUserService;
    @Autowired
    private CertUserInfoService certUserInfoService;

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
            logger.error(logHeader + "正在运行！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        isRun = true;
        String msgBody = new String(message.getBody());
        logger.info(logHeader + "接收到的消息：" + msgBody);
        // 不用传递消息 自己查询就好了
        // --> 消息处理
        try {
            for (int z = 0; z < 2; z++) {
                logger.info(logHeader + "开始循环第" + z + "次" + " 当前时间：" + GetDate.getNowTime10());
                // 查询前多少条标的数据
                List<CertBorrow> users = this.certOldBorrowUserService.insertCertBorrowUsers();
                logger.info(logHeader + "等待处理的用户数量:" + users == null ? "0" : users.size() + " 当前时间：" + GetDate.getNowTime10());
                List<CertSendUser> sendUser = new ArrayList<>();
                if (users != null && users.size() > 0) {
                    JSONArray result = new JSONArray();
                    for (CertBorrow item : users) {
                        CertSendUser certSendUser = certUserInfoService.getCertSendUserByUserId(item.getBorrowUserId());
                        Map<String, Object> param = certUserInfoService.getUserData(certSendUser, item.getBorrowNid(), CertCallConstant.CERT_PARAM_USER_STATUS_ADD);
                        result.add(param);
                        sendUser.add(certSendUser);
                    }
                    logger.info(logHeader + "组装数据完成，上报数量" + (result == null ? "0" : result.size()) + " 当前时间：" + GetDate.getNowTime10());

                    // 重新把结果分多个批次上传  按照月份
                    // 对象里面的格式   tradeDate 为年月日yyyyMM
                    List<CertReportEntity> entitys = CertCallUtil.groupByDate(result, thisMessName, CertCallConstant.CERT_INF_TYPE_USER_INFO);

                    for (CertReportEntity entity : entitys) {
                        // 上送数据
                        try {
                            entity = certUserInfoService.insertAndSendPost(entity);
                        } catch (Exception e) {
                            throw e;
                        }
                        logger.info(logHeader + "上报完成,数量为:" + entity.getDataList().size() + "，结果为:" + entity.getRetMess());
                        // 批量保存用户数据
                        if (entity != null && CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(entity.getReportStatus())) {
                            // 循环保存所有数据
                            List<CertUser> certUsers = new ArrayList<>();
                            for (int i = 0; i < result.size(); i++) {
                                Map<String, Object> param = (Map<String, Object>) result.get(i);
                                CertBorrow user = users.get(i);
                                CertSendUser certSendUser = sendUser.get(i);
                                CertUser certUser = new CertUser();
                                certUser.setBorrowNid(user.getBorrowNid());
                                certUser.setUserId(user.getBorrowUserId());
                                certUser.setUserIdCardHash((String) param.get("userIdcardHash"));
                                certUser.setHashValue(certSendUser.getUserIdcardValue());
                                certUser.setLogOrdId(entity.getLogOrdId());
                                certUsers.add(certUser);
                            }
                            certUserInfoService.insertCertUserByList(certUsers);
                        }
                        // 批量修改状态  start
                        List<Integer> ids = new ArrayList<>();
                        for (CertBorrow item : users) {
                            ids.add(item.getId());
                        }
                        if (ids.size() > 0) {
                            CertBorrowUpdate update = new CertBorrowUpdate();
                            update.setIds(ids);
                            CertBorrow certBorrow = new CertBorrow();
                            if (entity != null && CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(entity.getReportStatus())) {
                                // 成功
                                certBorrow.setIsUserInfo(1);
                            } else {
                                // 失败
                                certBorrow.setIsUserInfo(99);
                            }
                            update.setCertBorrow(certBorrow);
                            // 批量修改
                            certOldBorrowUserService.updateCertBorrowStatusBatch(update);
                        }
                        // 批量修改状态  end
                    }
                }else{
                    logger.info(logHeader + " 所有用户已经上报完成。");
                    break;
                }
            }
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。");
            isRun = false;
        }
    }
}
