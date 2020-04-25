/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.userinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.scatterinvest.CertScatterInveService;
import com.hyjf.mqreceiver.hgdatareport.cert.status.CertBorrowStatusService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.customize.CertSendUser;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 用户数据推送上报（延时队列）
 * @Author sunss
 * @Date 2018/11/26 17:57
 */
public class CertUserInfoMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(CertUserInfoMessageHadnle.class);

    private String thisMessName = "用户数据推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    private CertUserInfoService certUserInfoService;
    @Autowired
    private CertScatterInveService certScatterInveService;
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
        String userId = jsonObject.getString("userId");
        if (StringUtils.isBlank(userId)) {
            logger.error(logHeader + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        // 检查redis的值是否允许运行 允许返回true  不允许返回false
        boolean canRun = certUserInfoService.checkCanRun();
        if(!canRun){
            logger.info(logHeader + "redis不允许上报！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        // --> 消息处理
        try {
            CertSendUser users = certUserInfoService.getCertSendUserByUserId(Integer.parseInt(userId));
            if (users == null){
                // 如果未开户  则不上报
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                logger.info(logHeader+"未查找到相关用户，userId:"+userId);
                return;
            }
            if (users.getBankOpenAccount().equals(0)){
                // 如果未开户  则不上报
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                logger.info(logHeader+"用户未开户，userId:"+userId);
                return;
            }
            // 查询已经上报过的用户
            // 用来判断是新增还是修改
            List<CertUser> certUser = new ArrayList<>();
            if(StringUtils.isBlank(borrowNid)){
                // 投资人  或者借款人解绑卡操作
                certUser = certUserInfoService.getCertUsersByUserId(Integer.parseInt(userId));
            }else{
                // 借款人
                CertUser oneUser = certUserInfoService.getCertUserByUserIdBorrowNid(Integer.parseInt(userId),borrowNid);
                if(oneUser!=null){
                    certUser.add(oneUser);
                }
            }
            // 如果是借款人   并且是解绑卡操作
            if (users.getUserAttr().equals(2) && (StringUtils.isBlank(borrowNid))) {
                // 修改手机号风险测评时候用
                if(certUser==null){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    logger.info(logHeader+"借款人未上报过，userId:"+userId);
                    return;
                }
            }
            // --> 调用service组装数据
            if(certUser==null){
                certUser = new ArrayList<>();
            }
            JSONArray data = certUserInfoService.getSendData(users,borrowNid,certUser);

            logger.info(logHeader+"需要操作的用户数量："+(certUser.size())+"  组装数据为："+data.toString());

            // 上送数据
            CertReportEntity entity = new CertReportEntity(thisMessName, CertCallConstant.CERT_INF_TYPE_USER_INFO, borrowNid, data);
            entity.setUserId(userId);
            try {
                // 掉单用
                if(tradeDate!=null&&!"".equals(tradeDate)){
                    entity.setTradeDate(tradeDate);
                }
                entity = certUserInfoService.insertAndSendPost(entity);
            } catch (Exception e) {
                logger.error(logHeader + "上报用户信息错误！userId:"+userId,e);
            }
            // 上报成功  保存到数据库 不管成功失败都插入
            //if(CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(entity.getReportStatus())){
            for (CertUser item:certUser) {
                if(item!=null && item.getId()==null){
                    // 新增
                    item.setUserName(users.getUsername());
                    item.setLogOrdId(entity.getLogOrdId());
                    certUserInfoService.insertCertUser(item);
                }
            }

            //}
            // 上报标的信息 tradedate是空的话  是手动处理
            if(borrowNid!=null&&!"".equals(borrowNid)&&
                    (tradeDate==null||"".equals(tradeDate))){
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }

                logger.info(logHeader+"开始调用标的信息");
                // --> 调用service组装数据
                JSONArray data2 = new JSONArray();
                Map<String, Object> param = certScatterInveService.getSendData(borrowNid,certUser.get(0).getUserIdCardHash());
                param.remove("groupByDate");
                data2.add(param);
                // 上送数据
                CertReportEntity entity2 = new CertReportEntity("散标数据推送", CertCallConstant.CERT_INF_TYPE_SCATTER_INVEST, borrowNid, data2);
                try {
                    entity2 = certScatterInveService.insertAndSendPost(entity2);
                } catch (Exception e) {
                    logger.error(logHeader + "上报散标信息错误！userId:"+userId+"  ,borrowNid:"+borrowNid,e);
                }
                logger.info(logHeader+"散标信息处理完毕");


                logger.info(logHeader+"开始调用标的状态");
                // --> 调用service组装数据
                JSONArray listRepay  = new JSONArray();
                Map<String,Object> mapParam = certBorrowStatusService.selectBorrowByBorrowNid(borrowNid,null,true,false);
                mapParam.remove("groupByDate");
                listRepay.add(mapParam);
                logger.info("数据："+listRepay);
                // 上送数据
                CertReportEntity entity3 = new CertReportEntity("散标状态信息推送", CertCallConstant.CERT_INF_TYPE_STATUS, borrowNid, listRepay);
                try {
                    certBorrowStatusService.insertAndSendPost(entity3);
                } catch (Exception e) {
                    logger.error(logHeader + "上报标的状态错误！userId:"+userId+"  ,borrowNid:"+borrowNid,e);
                }
                logger.info(logHeader+"标的状态处理完毕");
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
