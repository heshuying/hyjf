/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.bifa.credittenderinfo;

import com.hyjf.mqreceiver.hgdatareport.bifa.BifaCommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.BifaCreditTenderInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo.BifaBorrowInfoService;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus.BifaBorrowStatusService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;

/**
 * 北互金-转让信息数据上报
 * jijun 20181129
 */
public class BifaCreditTenderInfoHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(BifaCreditTenderInfoHandle.class);

    private String thisMessName = "转让信息上报";
    private String logHeaderCreditTenderInfo = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaCreditTenderInfoService bifaCreditTenderInfoService;

    @Autowired
    private BifaBorrowInfoService bifaBorrowInfoService;

    @Autowired
    private BifaBorrowStatusService bifaBorrowStatusService;
    
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info(logHeaderCreditTenderInfo + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            _log.error(logHeaderCreditTenderInfo + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        _log.info(logHeaderCreditTenderInfo + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.error(logHeaderCreditTenderInfo + "解析消息体失败！！！", e);
            return;
        }

        String creditNid = jsonObject.getString("creditNid");
        Integer flag = jsonObject.getInteger("flag");
        if (StringUtils.isBlank(creditNid) || flag == null) {
            _log.error(logHeaderCreditTenderInfo + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            if (CustomConstants.BORROW_CREDIT_STATUS.equals(flag)){
            	//1散标完全承接 2三天到期有承接 3还款停止有承接 4后台停止后有承接
                // 获取散标债转信息
                BorrowCredit borrowCredit =this.bifaCreditTenderInfoService.selectBorrowCreditInfo(creditNid);
                if (null == borrowCredit) {
                    throw new Exception(logHeaderCreditTenderInfo + "未获取到债转信息！！"+"creditNid:"+creditNid);
                }

                //散标转让
                //债转本金=已认购本金(1.完全承接)
                //债转本金≠已认购本金并且已认购本金!=0(2.后台手动停止后有承接3.散标转让到期有承接)
                //先校验是否符合上报条件
                BifaCreditTenderInfoEntity bifaCreditInfoEntity = this.bifaCreditTenderInfoService.getBifaBorrowCreditInfoFromMongDB(BifaCommonConstants.HZR +borrowCredit.getCreditNid());
                if (null != bifaCreditInfoEntity) {
                    // 已经上报成功
                    _log.info(logHeaderCreditTenderInfo + " 已经上报。" + msgBody);
                    return;
                }
                if (null == bifaCreditInfoEntity) {
                    //出让人信息
                    UsersInfo creditUserInfo = this.bifaCreditTenderInfoService.getUsersInfoByUserId(borrowCredit.getCreditUserId());
                    if (null == creditUserInfo){
                        throw new Exception(logHeaderCreditTenderInfo + "未获取到出让人信息！！"+"userId:"+borrowCredit.getCreditUserId());
                    }

                    // -->先校验该散标信息是否上报,没有上报则执行上报操作
//                    Borrow borrow = this.bifaBorrowInfoService.checkBorrowInfoIsReported(borrowCredit.getBidNid());
//                    // -->再校验产品状态更新是否上报
//                    this.bifaBorrowStatusService.checkBifaBorrowStatusIsReported(borrow);
                    Borrow borrow = this.bifaBorrowInfoService.getBorrowByBorrowNid(borrowCredit.getBidNid());
                    if (null == borrow) {
                        throw new Exception(logHeaderCreditTenderInfo + "未获取到该债转的原始标的信息！！borrowNid："+borrowCredit.getBidNid());
                    }

                    bifaCreditInfoEntity = new BifaCreditTenderInfoEntity();
                    // --> 数据变换
                    boolean result = this.bifaCreditTenderInfoService.convertBifaBorrowCreditInfo(borrowCredit,borrow,creditUserInfo,bifaCreditInfoEntity);
                    if (!result){
                        throw new Exception(logHeaderCreditTenderInfo + "数据变换失败！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }
                    // --> 上报数据（实时上报）
                    //上报数据失败时 将数据存放到mongoDB
                    String methodName = "productRegistration";
                    BifaCreditTenderInfoEntity reportResult = this.bifaCreditTenderInfoService.reportData(methodName,bifaCreditInfoEntity);
                    if ("9".equals(reportResult.getReportStatus())) {
                        _log.error(logHeaderCreditTenderInfo + "上报数据失败！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }else if ("1".equals(reportResult.getReportStatus())){
                        _log.info(logHeaderCreditTenderInfo + "上报数据成功！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }
                    // --> 保存转让类型到本地mongo
                    bifaCreditInfoEntity.setFlag(flag);
                    // --> 保存上报数据
                    result = this.bifaCreditTenderInfoService.insertReportData(bifaCreditInfoEntity);
                    if (!result) {
                        _log.error(logHeaderCreditTenderInfo + "上报数据保存本地失败！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }else {
                        _log.info(logHeaderCreditTenderInfo + "上报数据保存本地成功！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }
                }
            }else if (CustomConstants.HJH_CREDIT_STATUS.equals(flag)){
                //智投转让
            	//1完全承接 2晚上结束债权 3还款
                HjhDebtCredit hjhDebtCredit =this.bifaCreditTenderInfoService.selectHjhDebtCreditInfo(creditNid);
                if (null == hjhDebtCredit) {
                    throw new Exception(logHeaderCreditTenderInfo + "未获取到智投转让信息！！"+"creditNid:"+creditNid);
                }

                BifaCreditTenderInfoEntity bifaCreditInfoEntity =
                        this.bifaCreditTenderInfoService.getBifaBorrowCreditInfoFromMongDB(BifaCommonConstants.HZR + hjhDebtCredit.getCreditNid());
                if (null != bifaCreditInfoEntity) {
                    // 已经上报成功
                    _log.info(logHeaderCreditTenderInfo + " 已经上报。" + msgBody);
                    return;
                }
                if (null == bifaCreditInfoEntity){
                    //出让人信息
                    UsersInfo creditUserInfo = this.bifaCreditTenderInfoService.getUsersInfoByUserId(hjhDebtCredit.getUserId());
                    if (null == creditUserInfo){
                        throw new Exception(logHeaderCreditTenderInfo + "未获取到出让人信息！！"+"userId:"+hjhDebtCredit.getUserId());
                    }
//                    // -->先校验该散标信息是否上报,没有上报则执行上报操作
//                    Borrow borrow = this.bifaBorrowInfoService.checkBorrowInfoIsReported(hjhDebtCredit.getBorrowNid());
//                    // -->再校验产品状态更新是否上报
//                    this.bifaBorrowStatusService.checkBifaBorrowStatusIsReported(borrow);
                    Borrow borrow = this.bifaBorrowInfoService.getBorrowByBorrowNid(hjhDebtCredit.getBorrowNid());
                    if (null == borrow) {
                        throw new Exception(logHeaderCreditTenderInfo + "未获取到该债转的原始标的信息！！borrowNid："+hjhDebtCredit.getBorrowNid());
                    }
                    
                    bifaCreditInfoEntity = new BifaCreditTenderInfoEntity();
                    // --> 数据变换
                    boolean result = this.bifaCreditTenderInfoService.convertBifaHjhCreditInfo(hjhDebtCredit,borrow,creditUserInfo,bifaCreditInfoEntity);
                    if (!result){
                        throw new Exception(logHeaderCreditTenderInfo + "数据变换失败！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }
                    // --> 上报数据（实时上报）
                    //上报数据失败时 将数据存放到mongoDB
                    String methodName = "productRegistration";
                    BifaCreditTenderInfoEntity reportResult = this.bifaCreditTenderInfoService.reportData(methodName,bifaCreditInfoEntity);
                    if ("9".equals(reportResult.getReportStatus())) {
                        _log.error(logHeaderCreditTenderInfo + "上报数据失败！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }else if ("1".equals(reportResult.getReportStatus())){
                        _log.info(logHeaderCreditTenderInfo + "上报数据成功！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }
                    
                    // --> 保存转让类型到本地mongo
                    bifaCreditInfoEntity.setFlag(flag);
                    // --> 保存上报数据
                    result = this.bifaCreditTenderInfoService.insertReportData(bifaCreditInfoEntity);
                    if (!result) {
                        _log.error(logHeaderCreditTenderInfo + "上报数据保存本地失败！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }else {
                        _log.info(logHeaderCreditTenderInfo + "上报数据保存本地成功！！"+JSONObject.toJSONString(bifaCreditInfoEntity));
                    }
                }
            }
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            _log.error(logHeaderCreditTenderInfo + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info(logHeaderCreditTenderInfo + " 结束。");
        }
    }
}
