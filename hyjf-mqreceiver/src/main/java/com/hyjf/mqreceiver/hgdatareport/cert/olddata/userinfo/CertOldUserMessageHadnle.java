/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.userinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.CertSendUser;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心 投资人老数据上报
 * @Author sunss
 * @Date 2018/11/26 17:57
 */
public class CertOldUserMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertOldUserMessageHadnle.class);

    private String thisMessName = "投资人老数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
    // 是否正在运行
    private static boolean isRun = false;

    @Autowired
    private CertOldUserService certOldUserService;
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
        // 不用传递消息 自己查询就好了
        // --> 消息处理
        try {
            for (int z = 0; z < 10; z++) {
                logger.info(logHeader + "开始循环第" + z + "次" + " 当前时间：" + GetDate.getNowTime10());
                List<CertSendUser> users = this.certOldUserService.insertCertUserNotSend();
                List<CertSendUser> sendUser = new ArrayList<>();
                logger.info(logHeader + "等待处理的用户数量:" + (users == null ? "0" : users.size()) + " 当前时间：" + GetDate.getNowTime10());
                if (users != null && users.size() > 0) {
                    JSONArray result = new JSONArray();
                    for (CertSendUser item : users) {
                        Map<String, Object> param = certUserInfoService.getUserData(item, null, CertCallConstant.CERT_PARAM_USER_STATUS_ADD);
                        result.add(param);
                        sendUser.add(item);
                    }
                    logger.info(logHeader + "组装数据完成，上报数量" + (result == null ? "0" : result.size()) + " 当前时间：" + GetDate.getNowTime10());

                    // 重新把结果分多个批次上传  按照月份
                    // 对象里面的格式   tradeDate 为年月日yyyyMM
                    List<CertReportEntity> entitys = CertCallUtil.groupByDate(result, thisMessName, CertCallConstant.CERT_INF_TYPE_USER_INFO);
                    // 遍历循环上报
                    for (CertReportEntity entity : entitys) {
                        try {
                            entity = certUserInfoService.insertAndSendPost(entity);
                            logger.info(logHeader + "上报完成,数量为:" + entity.getDataList().size() + "，结果为:" + entity.getRetMess());
                            // 循环保存所有数据
                            if (entity != null && CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(entity.getReportStatus())) {
                                List<CertUser> certUsers = new ArrayList<>();
                                for (int i = 0; i < sendUser.size(); i++) {
                                    Map<String, Object> param = (Map<String, Object>) result.get(i);
                                    CertSendUser user = sendUser.get(i);
                                    CertUser certUser = new CertUser();
                                    certUser.setBorrowNid(null);
                                    certUser.setUserId(user.getUserId());
                                    certUser.setUserName(user.getUsername());
                                    certUser.setUserIdCardHash((String) param.get("userIdcardHash"));
                                    certUser.setHashValue(user.getUserIdcardValue());
                                    certUsers.add(certUser);
                                    certUser.setLogOrdId(entity.getLogOrdId());
                                }
                                certUserInfoService.insertCertUserByList(certUsers);
                            }
                        } catch (Exception e) {
                            throw e;
                        }
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
