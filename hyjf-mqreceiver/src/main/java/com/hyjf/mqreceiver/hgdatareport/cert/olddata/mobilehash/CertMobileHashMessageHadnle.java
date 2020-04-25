/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.mobilehash;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.userinfo.CertOldUserService;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.userinfo.CertOldUserServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.CertMobileHash;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.CertSendUser;
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
 * @Description 合规数据上报 CERT 国家互联网应急中心 手机号哈希
 * @Author sunss
 * @Date 2018/11/26 17:57
 */
public class CertMobileHashMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertMobileHashMessageHadnle.class);

    private String thisMessName = "手机号哈希";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    private CertOldUserService certOldUserService;

    /**
     * 是否正在运行
     */
    private static boolean isRun = false;

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
            logger.error(logHeader + "正在运行！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        isRun = true ;
        String msgBody = new String(message.getBody());
        // 不用传递消息 自己查询就好了
        // --> 消息处理
        try {
            // 获取没有进行哈希的用户
            List<Users> users = certOldUserService.getNotHashUsers();
            logger.info(logHeader +"待处理的用户数量:"+(users==null?"0":users.size()));
            List<CertMobileHash> mobileHashes = new ArrayList<>();
            for (Users item : users) {
                try{
                    JSONObject hashMobile = CertCallUtil.phoneHash(item.getMobile());
                    CertMobileHash moble = new CertMobileHash();
                    moble.setMobile(item.getMobile());
                    moble.setPhonehash(hashMobile.getString("phone"));
                    moble.setUserId(item.getUserId());
                    moble.setSalt(hashMobile.getString("salt"));
                    mobileHashes.add(moble);
                }catch (Exception e){
                    logger.error(logHeader+"加密手机号出错,手机号为:"+item.getMobile());
                }
            }
            logger.info(logHeader +"待插入的用户数量:"+(mobileHashes==null?"0":mobileHashes.size()));
            if(mobileHashes!=null && mobileHashes.size()>0){
                certOldUserService.insertMobileHashBatch(mobileHashes);
            }
            logger.info(logHeader +"操作成功，插入数量为:"+(mobileHashes==null?"0":mobileHashes.size()));
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            isRun = false;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。");
        }
    }
}
